package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeCreateDTO {
    private Long idMonopatin;
    private Long idUsuario;
    private Long paradaInicio;
}
