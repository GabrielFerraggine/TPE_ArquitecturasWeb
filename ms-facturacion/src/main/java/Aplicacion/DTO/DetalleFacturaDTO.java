package Aplicacion.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
public class DetalleFacturaDTO {

    private Long facturaId;
    private Long viajeId;
    private BigDecimal tarifaBase;
    private BigDecimal tarifaExtra;
    private Long tiempoUso;
    private Long tiempoPausado;

    private String tipoCuenta;
    private Double kmAcumuladosMes;
}
