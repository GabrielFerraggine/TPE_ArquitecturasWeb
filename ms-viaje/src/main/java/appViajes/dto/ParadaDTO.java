package appViajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParadaDTO {
    private Long id;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private Boolean activa = true;
    private Double radioPermitidoMetros = 50.0;
}