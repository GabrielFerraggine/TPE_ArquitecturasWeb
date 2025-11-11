package appViajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {
    private Long id;
    private Long idMonopatin;
    private Long idUsuario;
    private Long idCuenta;
    private LocalDateTime  fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Double kmRecorridos;
    private Double taifa;
    private Long paradaInicio;
    private Long paradaFinal;
    private String estado;
    private List<PausaDTO> pausas;
}
