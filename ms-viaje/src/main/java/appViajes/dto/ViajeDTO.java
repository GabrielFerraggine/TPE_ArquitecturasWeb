package appViajes.dto;

import appViajes.entity.Viaje;
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

    public ViajeDTO(Viaje viaje) {
       this.id = viaje.getId();
       this.idMonopatin = viaje.getIdMonopatin();
       this.idUsuario = viaje.getIdUsuario();
       this.idCuenta = viaje.getIdCuenta();
       this.fechaHoraInicio = viaje.getFechaHoraInicio();
       this.fechaHoraFin = viaje.getFechaHoraFin();
       this.kmRecorridos = viaje.getKmRecorridos();
       this.taifa = viaje.getTaifa();
       this.paradaInicio = viaje.getParadaInicio();
       this.paradaFinal = viaje.getParadaFinal();
       this.estado = viaje.getEstado().toString();
    }

}
