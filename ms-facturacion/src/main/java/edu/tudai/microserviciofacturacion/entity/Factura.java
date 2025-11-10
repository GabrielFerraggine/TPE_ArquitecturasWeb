package edu.tudai.microserviciofacturacion.entity;

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

    private Long usuarioId;
    private BigDecimal montoTotal;
    private LocalDate fechaEmision;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detallesFactura = new java.util.ArrayList<>();


    //recorre detallesFactura y suma el montoCalculado de cada detalle
    public BigDecimal calcularMontoTotal() {
        return detallesFactura.stream()
                .map(DetalleFactura::getMontoCalculado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //Agrega un detalle a la factura y actualiza el monto total.
    public void agregarDetalle(DetalleFactura detalle) {
        detalle.setFactura(this);  // Establecer relación bidireccional
        detallesFactura.add(detalle);
        recalcularMontoTotal();
    }

    //Recalcula el monto total basado en los detalles de la factura.
    public void recalcularMontoTotal() {
        this.montoTotal = calcularMontoTotal();
    }
}
