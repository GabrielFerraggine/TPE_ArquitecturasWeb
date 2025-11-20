package gateway.controller;

import gateway.DTOs.UsuarioDTO;
import gateway.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UsuarioService usuarioService;

    @PostMapping("")
    public ResponseEntity<?> saveUser( @RequestBody @Valid UsuarioDTO usuarioDTO) {
        log.info("Received request to create user: {}", usuarioDTO.getUsername());
        try {
            final var id = usuarioService.saveUser(usuarioDTO);
            log.info("User created successfully with ID: {}", id);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
        }
    }
}
