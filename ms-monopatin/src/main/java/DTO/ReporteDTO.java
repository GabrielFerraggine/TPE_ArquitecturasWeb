package DTO;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDTO {
    private int idMonopatin;
    private double kmRecorridos;
    private int tiempoDeUsoTotal;
    private int tiempoDePausas;
}
