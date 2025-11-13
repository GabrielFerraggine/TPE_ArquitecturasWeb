package appViajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IniciarViajeRequest {
    private Long idMonopatin;
    private Long idUsuario;
    private Long idCuenta;
    private Long paradaInicio;
    private Long paradaFinal;

    public void setIdParadaInicio(Long idParadaInicio) {
        this.paradaInicio = idParadaInicio;
    }
    public void setIdParadaFinal(Long idParadaFinal) {
        this.paradaFinal = idParadaFinal;
    }

    public void setIdMonopatin(Long idMonopatin) {
        this.idMonopatin = idMonopatin;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

}