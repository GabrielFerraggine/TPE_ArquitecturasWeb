package Modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class Facturacion {
    private Long id;
    private Long usuarioId;
    private Double montoTotal;
    private LocalDate fechaEmision;
    private List<DetalleFactura> detallesFactura;
}
