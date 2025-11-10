package Aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SaldoResponseDTO {
    private Long idCuenta;
    private BigDecimal monto;
}
