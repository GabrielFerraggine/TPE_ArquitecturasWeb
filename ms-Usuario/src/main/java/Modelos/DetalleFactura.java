package Modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetalleFactura {
    private Long id;
    private Facturacion factura;
    private Long viajeId;
    private Double tarifaBase;
    private Double tarifaExtra;
    private Long tiempoUso;
    private Long tiempoPausado;
    private Double montoCalculado;

}
