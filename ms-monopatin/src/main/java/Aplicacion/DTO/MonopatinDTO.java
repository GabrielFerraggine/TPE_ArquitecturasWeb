package Aplicacion.DTO;

import Aplicacion.entity.Monopatin;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinDTO {
    private Long idMonopatin;
    private String estado; // enUso, enMantenimiento, libre
    private double latitud;
    private double longitud;
    private double kmRecorridos;
    private int tiempoDeUsoTotal;
    private int tiempoDePausas;

    public MonopatinDTO(Monopatin monopatin) {
        this.idMonopatin = monopatin.getIdMonopatin();
        this.kmRecorridos = monopatin.getKmRecorridos();
        this.tiempoDeUsoTotal = monopatin.getTiempoDeUsoTotal();
        this.estado = monopatin.getEstado();
        this.longitud = monopatin.getLongitud();
        this.latitud = monopatin.getLatitud();
    }

}
