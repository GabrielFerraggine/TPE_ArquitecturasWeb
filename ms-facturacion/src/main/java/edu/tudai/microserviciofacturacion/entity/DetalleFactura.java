package edu.tudai.microserviciofacturacion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    @JsonIgnore
    private Factura factura;

    private Long viajeId;
    private Double tarifaBase;
    private Double tarifaExtra;
    private Long tiempoUso;
    private Long tiempoPausado;
    private Double montoCalculado;

    //se crea construnctor con 6 parametros(no uso el AllArgs de lombok)
    public DetalleFactura(Factura factura, Long viajeId, double tarifaBase, double tarifaExtra, long tiempoUso, long tiempoPausado) {
        this.factura = factura;
        this.viajeId = viajeId;
        this.tarifaBase = tarifaBase;
        this.tarifaExtra = tarifaExtra;
        this.tiempoUso = tiempoUso;
        this.tiempoPausado = tiempoPausado;
    }

    public void calcularMonto(){
        Double costoBase = tarifaBase * tiempoUso;
        Double costoExtra = tiempoPausado > 15 ? tarifaExtra * (tiempoPausado - 15) : 0;
        this.montoCalculado = costoBase + costoExtra;
    }

}
