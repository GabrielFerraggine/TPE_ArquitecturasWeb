package Aplicacion.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaldoRequestDTO {
    @NotNull
    @Min(1)
    private BigDecimal monto;
}
