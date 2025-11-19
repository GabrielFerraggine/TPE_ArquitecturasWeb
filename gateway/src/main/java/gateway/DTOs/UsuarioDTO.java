package gateway.DTOs;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
public class UsuarioDTO {
    @NotNull( message = "El usuario es un campo obligatorio.")
    @NotEmpty( message = "El usuario es un campo obligatorio.")
    private String username;

    @NotNull( message = "La contraseña es un campo obligatorio.")
    @NotEmpty( message = "La contraseña es un campo obligatorio.")
    private String password;

    @NotNull( message = "Los roles son un campo obligatorio.")
    @NotEmpty( message = "Los roles son un campo obligatorio.")
    private Set<String> admins;
}
