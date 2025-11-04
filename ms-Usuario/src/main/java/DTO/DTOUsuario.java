package DTO;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
public class DTOUsuario {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private List<Cuenta> cuentas;
    private String mail;
    private Enum rol;
    private int latitud;
    private int longitud;
    private List<Monopatin> monopatines;
    private List<Viaje> viajes;
 }
