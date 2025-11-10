package DTO;

import Entidades.Rol;
import lombok.*;

@Data
@AllArgsConstructor
public class DTOUsuario {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    //private List<Cuenta> cuentas;
    private String mail;
    private Rol rol;
    private double latitud;
    private double longitud;
    //private List<Monopatin> monopatines;
    //private List<Viaje> viajes;

 }
