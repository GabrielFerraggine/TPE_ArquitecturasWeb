package edu.tudai.microserviciofacturacion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Double montoTotal;
    private LocalDate fechaEmision;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detallesFactura = new java.util.ArrayList<>();


    //recorre detallesFactura y suma el montoCalculado de cada detalle
    public double calcularMontoTotal() {
        return detallesFactura.stream()
                .mapToDouble(DetalleFactura::getMontoCalculado)
                .sum();
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
