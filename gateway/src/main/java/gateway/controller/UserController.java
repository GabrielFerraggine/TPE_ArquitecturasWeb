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


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> saveUser( @RequestBody @Valid UsuarioDTO usuarioDTO) {
        final var id = usuarioService.saveUser( usuarioDTO );
        return new ResponseEntity<>( id, HttpStatus.CREATED );
    }
}
