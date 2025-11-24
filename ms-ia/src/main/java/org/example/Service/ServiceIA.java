package org.example.Service;

import org.example.Client.GroqClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.feignClient.CuentaFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

@Service
public class ServiceIA {
    private static final Logger log = LoggerFactory.getLogger(ServiceIA.class);

    @Autowired
    private GroqClient groqClient;

    @Autowired
    private CuentaFeignClient cuentaFeignClient;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String URL_MS_FACTURACION = "http://localhost:8004/api/factura";
    private final String URL_MS_VIAJE = "http://localhost:8003/api/viajes";

    private static final String SECRET = "j7ZookpUTYxclaULynjypGQVKMYXqOXMI+/1sQ2gOV1BF6VOHw6OzYj9RNZY4GcHAE3Igrah3MZ26oLrY/3y4Q==";

    private List<Map<String, Object>> obtenerDefinicionTools() {
        return List.of(
                Map.of(
                        "type", "function",
                        "function", Map.of(
                                "name", "obtener_total_facturado",
                                "description", "Obtiene el monto total facturado por la empresa en un rango de fechas.",
                                "parameters", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "anio", Map.of("type", "integer"),
                                                "mesInicio", Map.of("type", "integer"),
                                                "mesFin", Map.of("type", "integer")
                                        ),
                                        "required", List.of("anio", "mesInicio", "mesFin")
                                )
                        )
                ),
                Map.of(
                        "type", "function",
                        "function", Map.of(
                                "name", "historial_viajes_usuario",
                                "description", "Obtiene el listado de viajes realizados por un usuario específico.",
                                "parameters", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "idUsuario", Map.of("type", "integer", "description", "ID del usuario a consultar (si no se especifica, usa el actual)")
                                        ),
                                        "required", List.of("idUsuario")
                                )
                        )
                ),
                Map.of(
                        "type", "function",
                        "function", Map.of(
                                "name", "reporte_viajes_frecuentes",
                                "description", "Obtiene una lista de los monopatines que han realizado MÁS de cierta cantidad de viajes en un año específico.",
                                "parameters", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "cantidadMinima", Map.of("type", "integer", "description", "La cantidad mínima de viajes para filtrar (ej: 10)"),
                                                "anio", Map.of("type", "integer", "description", "El año a consultar (ej: 2024)")
                                        ),
                                        "required", List.of("cantidadMinima", "anio")
                                )
                        )
                )
        );
    }

    public ResponseEntity<?> procesarMensaje(String prompt, String token) {
        try {
            Map<String, Object> respuestaIa = groqClient.preguntarConTools(prompt, obtenerDefinicionTools());

            if ((boolean) respuestaIa.get("esFuncion")) {
                String nombreFuncion = (String) respuestaIa.get("nombreFuncion");
                String argumentosJson = (String) respuestaIa.get("argumentos");

                log.info("🤖 Ejecutando tool: {}", nombreFuncion);
                Object resultadoApi = ejecutarToolReal(nombreFuncion, argumentosJson, token);

                String jsonResultado = objectMapper.writeValueAsString(resultadoApi);

                String promptResumen = String.format(
                        "El usuario preguntó: \"%s\".\n" +
                                "Usé la herramienta \"%s\" y obtuve estos datos crudos: %s.\n" +
                                "Por favor, responde al usuario de forma natural, útil y breve basándote en esos datos. " +
                                "No menciones IDs técnicos ni JSON, habla como un asistente humano.",
                        prompt, nombreFuncion, jsonResultado
                );

                Map<String, Object> respuestaFinal = groqClient.preguntarConTools(promptResumen, List.of());

                return ResponseEntity.ok(Map.of(
                        "respuesta", respuestaFinal.get("contenido")
                ));

            } else {
                return ResponseEntity.ok(Map.of(
                        "respuesta", respuestaIa.get("contenido")
                ));
            }

        } catch (Exception e) {
            log.error("Error en IaService", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    private Object ejecutarToolReal(String nombreFuncion, String jsonArgs, String token) throws Exception {
        Map<String, Object> args = objectMapper.readValue(jsonArgs, Map.class);

        switch (nombreFuncion) {
            case "obtener_total_facturado":
                Integer anio = ((Number) args.get("anio")).intValue();
                Integer mesInicio = ((Number) args.get("mesInicio")).intValue();
                Integer mesFin = ((Number) args.get("mesFin")).intValue();
                String urlFact = URL_MS_FACTURACION + "/totalFacturado/" + anio + "/" + mesInicio + "/" + mesFin;
                return restTemplate.getForObject(urlFact, Double.class);

            case "historial_viajes_usuario":
                Integer idUser = args.containsKey("idUsuario") ? ((Number) args.get("idUsuario")).intValue() : 1;
                String urlViajes = URL_MS_VIAJE + "/usuario/" + idUser;
                return restTemplate.getForObject(urlViajes, List.class);

            case "reporte_viajes_frecuentes":
                Integer cant = ((Number) args.get("cantidadMinima")).intValue();
                Integer anioReporte = ((Number) args.get("anio")).intValue();
                String urlReporte = URL_MS_VIAJE + "/viajesFrecuentes/" + cant + "/" + anioReporte;
                return restTemplate.getForObject(urlReporte, List.class);

            default:
                return "Lo siento, la función " + nombreFuncion + " no está soportada aún.";
        }
    }

    public boolean esUsuarioPremium(String token) {
        try {
            return(cuentaFeignClient.verificarCuentaPremium(1L));
        } catch (Exception e) {
            return false;
        }
    }
}
