package org.example.Service;

import org.example.Client.GroqClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    // Cliente HTTP para llamar a ms-facturacion, ms-viaje, etc.
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- URLs de tus otros Microservicios (Ajusta los puertos si cambian) ---
    private final String URL_MS_FACTURACION = "http://localhost:8004/api/factura";
    private final String URL_MS_VIAJE = "http://localhost:8003/api/viajes";
    private final String URL_MS_MONOPATIN = "http://localhost:8005/api/monopatin";

    /*
    private static final String SECRET = "j7ZookpUTYxclaULynjypGQVKMYXqOXMI+/1sQ2gOV1BF6VOHw6OzYj9RNZY4GcHAE3Igrah3MZ26oLrY/3y4Q==";
    private static final String AUTHORITIES_KEY = "auth"; */
    /**
     * 1. Define las "Tools" que la IA puede usar (Menú de opciones)
     */
    private List<Map<String, Object>> obtenerDefinicionTools() {
        return List.of(
                // TOOL 1: Facturación (La que ya tenías)
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
                // TOOL 2: Monopatines Cercanos (NUEVA)
                Map.of(
                        "type", "function",
                        "function", Map.of(
                                "name", "buscar_monopatines_cercanos",
                                "description", "Busca monopatines disponibles cerca de una ubicación (latitud/longitud).",
                                "parameters", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "latitud", Map.of("type", "number", "description", "Latitud geográfica"),
                                                "longitud", Map.of("type", "number", "description", "Longitud geográfica")
                                        ),
                                        "required", List.of("latitud", "longitud")
                                )
                        )
                ),
                // TOOL 3: Historial de Viajes (ACTUALIZADA con tu controller real)
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

    /**
     * 2. Procesa el prompt del usuario
     */
    public ResponseEntity<?> procesarMensaje(String prompt, String token) {
        try {
            // --- PASO 1: Preguntar a la IA qué hacer ---
            Map<String, Object> respuestaIa = groqClient.preguntarConTools(prompt, obtenerDefinicionTools());

            if ((boolean) respuestaIa.get("esFuncion")) {
                // CASO 1: La IA decidió usar una herramienta
                String nombreFuncion = (String) respuestaIa.get("nombreFuncion");
                String argumentosJson = (String) respuestaIa.get("argumentos");

                log.info("🤖 Ejecutando tool: {}", nombreFuncion);
                Object resultadoApi = ejecutarToolReal(nombreFuncion, argumentosJson, token);

                // --- PASO 2 (NUEVO): VOLVER A LLAMAR A LA IA PARA RESUMIR ---

                // Convertimos el resultado crudo a texto para que la IA lo lea
                String jsonResultado = objectMapper.writeValueAsString(resultadoApi);

                // Creamos un nuevo prompt interno
                String promptResumen = String.format(
                        "El usuario preguntó: \"%s\".\n" +
                                "Usé la herramienta \"%s\" y obtuve estos datos crudos: %s.\n" +
                                "Por favor, responde al usuario de forma natural, útil y breve basándote en esos datos. " +
                                "No menciones IDs técnicos ni JSON, habla como un asistente humano.",
                        prompt, nombreFuncion, jsonResultado
                );

                // Llamamos a Groq de nuevo (esta vez SIN tools, solo para charlar)
                Map<String, Object> respuestaFinal = groqClient.preguntarConTools(promptResumen, List.of());

                // Devolvemos la respuesta bonita
                return ResponseEntity.ok(Map.of(
                        "respuesta", respuestaFinal.get("contenido")
                ));

            } else {
                // CASO 2: Charla normal
                return ResponseEntity.ok(Map.of(
                        "respuesta", respuestaIa.get("contenido")
                ));
            }

        } catch (Exception e) {
            log.error("Error en IaService", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * 3. Switch para ejecutar la llamada HTTP real al microservicio correspondiente
     */
    private Object ejecutarToolReal(String nombreFuncion, String jsonArgs, String token) throws Exception {
        Map<String, Object> args = objectMapper.readValue(jsonArgs, Map.class);

        switch (nombreFuncion) {

            // CASO A: FACTURACIÓN (Ya lo tenías, lo dejo igual)
            case "obtener_total_facturado":
                Integer anio = ((Number) args.get("anio")).intValue();
                Integer mesInicio = ((Number) args.get("mesInicio")).intValue();
                Integer mesFin = ((Number) args.get("mesFin")).intValue();
                String urlFact = URL_MS_FACTURACION + "/totalFacturado/" + anio + "/" + mesInicio + "/" + mesFin;
                return restTemplate.getForObject(urlFact, Double.class);

            // CASO C: HISTORIAL DE VIAJES (Nuevo)
            // Endpoint: /api/viajes/usuario/{idUsuario}
            case "historial_viajes_usuario":
                // Intentamos sacar el ID del prompt, si no viene, usamos 1 por defecto (o podrías sacarlo del token)
                Integer idUser = args.containsKey("idUsuario") ? ((Number) args.get("idUsuario")).intValue() : 1;

                String urlViajes = URL_MS_VIAJE + "/usuario/" + idUser;

                // Retorna la lista de ViajeDTO
                return restTemplate.getForObject(urlViajes, List.class);

            case "reporte_viajes_frecuentes":
                Integer cant = ((Number) args.get("cantidadMinima")).intValue();
                Integer anioReporte = ((Number) args.get("anio")).intValue();

                // Construimos la URL: /api/viajes/viajesFrecuentes/{cantidad}/{anio}
                String urlReporte = URL_MS_VIAJE + "/viajesFrecuentes/" + cant + "/" + anioReporte;

                // Retornamos la lista de mapas
                return restTemplate.getForObject(urlReporte, List.class);

            default:
                return "Lo siento, la función " + nombreFuncion + " no está soportada aún.";
        }
    }

/*
    public boolean esUsuarioPremium(String token) {
        try {
            // 1. Limpiar el token (quitar "Bearer " si viene)
            String jwtReal = token;
            if (token != null && token.startsWith("Bearer ")) {
                jwtReal = token.substring(7);
            }

            // 2. Parsear el Token usando la misma clave secreta
            byte[] keyBytes = Decoders.BASE64.decode(SECRET);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(jwtReal)
                    .getBody();

            // 3. Extraer los roles (vienen como String separados por coma)
            // Ejemplo: "ROLE_USER,ROLE_PREMIUM"
            String roles = claims.get(AUTHORITIES_KEY, String.class);

            // 4. Validar si tiene el rol deseado
            // Asegúrate de usar el nombre exacto que tienes en tu DB
            return roles != null && roles.contains("ROLE_PREMIUM");

        } catch (Exception e) {
            // Si el token está vencido, manipulado o inválido, asumimos que NO es premium
            // e.printStackTrace(); // Descomentar solo para debug
            return false;
        }
    } */
    public boolean esUsuarioPremium(String token){
        return true;
    }
}
