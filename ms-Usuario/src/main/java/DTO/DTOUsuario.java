package DTO;

import Entidades.*;
import lombok.*;
import java.util.*;

@Data
@AllArgsConstructor
public class DTOUsuario {
    private String idUsuario;
    private String nombre;
    private String apellido;
    private String mail;
    private Rol rol;
    private double latitud;
    private double longitud;

}
