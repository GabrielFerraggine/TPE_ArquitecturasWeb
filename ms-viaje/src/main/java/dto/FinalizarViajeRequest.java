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
}
