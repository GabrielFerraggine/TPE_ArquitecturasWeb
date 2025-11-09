package dto;

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

    // Con esto encapsulamos los datos necesarios para iniciar un viaje

}
