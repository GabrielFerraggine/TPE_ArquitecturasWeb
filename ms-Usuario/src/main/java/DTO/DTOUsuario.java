package DTO;

import Entidades.*;
import lombok.*;
import java.util.*;

@Data
@AllArgsConstructor
public class DTOUsuario {
    private String idUsuario; //ESTO TIENE QUE SER LONG ? //Peter 1
    private String nombre;
    private String apellido;
    private List<Cuenta> cuentas;
    private String mail;
    private Rol rol;
    private double latitud;
    private double longitud;
    private List<Long> monopatines;
    private List<Long> viajes;

}
