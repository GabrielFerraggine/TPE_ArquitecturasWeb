package Modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Monopatin {
    private Long idMonopatin;
    private Estado estado; // enUso, enMantenimiento, libre
    private double latitud;
    private double longitud;
    private double kmRecorridos;
    private int tiempoDeUsoTotal;
    private int tiempoDePausas;

    public enum Estado {
        EN_USO,
        EN_MANTENIMIENTO,
        LIBRE
    }
}
