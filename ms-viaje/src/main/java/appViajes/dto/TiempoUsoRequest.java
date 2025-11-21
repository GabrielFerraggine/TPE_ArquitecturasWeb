package appViajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiempoUsoRequest {
    private Long idUsuario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean verCuentasRelacionadas = false;



}
