package Modelos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Viaje {
    private Long id;
    private Long idMonopatin;
    private Long idUsuario;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Double kmRecorridos;
    private Double taifa;
    private Long paradaInicio;
    private Long paradaFinal;
}
