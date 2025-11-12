package Aplicacion.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDTO {
    private Long idMonopatin;
    private double kmRecorridos;
    private int tiempoDeUsoTotal;
    private int tiempoDePausas;

}
