package Aplicacion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long usuarioId;

    @Column
    private BigDecimal montoTotal;

    @Column
    private LocalDate fechaEmision;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detallesFactura = new java.util.ArrayList<>();

    public BigDecimal calcularMontoTotal() {
        return detallesFactura.stream()
                .map(DetalleFactura::getMontoCalculado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void agregarDetalle(DetalleFactura detalle) {
        detalle.setFactura(this);  // Establecer relación bidireccional
        detallesFactura.add(detalle);
        recalcularMontoTotal();
    }

    public void recalcularMontoTotal() {
        this.montoTotal = calcularMontoTotal();
    }
}
