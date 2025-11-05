package edu.tudai.microserviciofacturacion.DTO;

import lombok.*;

@Data
public class DetalleFacturaDTO {

    private Long facturaId;
    private Long viajeId;
    private Double tarifaBase;
    private Double tarifaExtra;
    private Long tiempoUso;
    private Long tiempoPausado;

    private String tipoCuenta;
    private Double kmAcumuladosMes;
}
