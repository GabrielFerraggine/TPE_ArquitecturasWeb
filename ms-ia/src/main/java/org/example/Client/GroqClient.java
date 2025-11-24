package org.example.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroqClient {
    private static final Logger log = LoggerFactory.getLogger(GroqClient.class);
    private final RestTemplate rest;
    private final String baseUrl;
    private final String apiKey;
    private final String model;

    public GroqClient(
            @Value("${groq.base-url:https://api.groq.com/openai}") String baseUrl,
            @Value("${groq.api-key}") String apiKey,
            @Value("${groq.model:llama3-70b-8192}") String model
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.model = model;
        this.rest = new RestTemplate();
    }

    // Método modificado para enviar TOOLS
    public Map<String, Object> preguntarConTools(String prompt, List<Map<String, Object>> tools) {
        String url = baseUrl + "/v1/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        // Inyectamos las herramientas disponibles
        body.put("tools", tools);
        body.put("tool_choice", "auto"); // Dejamos que la IA decida

        if (tools != null && !tools.isEmpty()) {
            body.put("tools", tools);
            body.put("tool_choice", "auto");
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> resp = rest.exchange(url, HttpMethod.POST, req, Map.class);
            Map<String, Object> responseBody = resp.getBody();

            // Parseo manual de la respuesta de OpenAI/Groq
            List<?> choices = (List<?>) responseBody.get("choices");
            Map<?, ?> choice0 = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice0.get("message");

            Map<String, Object> resultado = new HashMap<>();

            // Verificamos si Groq quiere usar una herramienta
            if (message.containsKey("tool_calls")) {
                List<?> toolCalls = (List<?>) message.get("tool_calls");
                Map<?, ?> firstTool = (Map<?, ?>) toolCalls.get(0);
                Map<?, ?> function = (Map<?, ?>) firstTool.get("function");

                resultado.put("esFuncion", true);
                resultado.put("nombreFuncion", function.get("name"));
                resultado.put("argumentos", function.get("arguments")); // Viene como String JSON
            } else {
                // Es una respuesta de texto normal
                resultado.put("esFuncion", false);
                resultado.put("contenido", message.get("content"));
            }
            return resultado;

        } catch (Exception e) {
            log.error("Error llamando a Groq API", e);
            throw new RuntimeException("Error de comunicación con el LLM");
        }
    }
}
