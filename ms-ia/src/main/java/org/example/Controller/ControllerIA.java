package org.example.Controller;

import org.example.Service.ServiceIA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class ControllerIA {
    @Autowired
    private ServiceIA serviceIa;

    @PostMapping(value = "/chat", produces = "application/json")
    public ResponseEntity<?> conversar(
            @RequestBody String prompt,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        try {
            // Validación de Usuario Premium
            if (token == null || !serviceIa.esUsuarioPremium(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Acceso denegado: Esta funcionalidad es exclusiva para usuarios PREMIUM.");
            }

            // Procesar el mensaje
            return serviceIa.procesarMensaje(prompt, token);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el chat: " + e.getMessage());
        }
    }
}
