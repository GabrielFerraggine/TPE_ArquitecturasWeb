package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalizarViajeRequest  {
    private Long idViaje;
    private Long paradaFinal;
    private Double kmRecorridos;
    private Double latitud;
    private Double longitud;



    // Con esto encapsulamos los datos necesarios para finalizar un viaje
}
