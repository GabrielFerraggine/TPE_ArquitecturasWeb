package edu.tudai.microserviciofacturacion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    private BigDecimal tarifaBase;
    private BigDecimal tarifaExtra;
    private Long tiempoUso;
    private Long tiempoPausado;
    private BigDecimal montoCalculado;

    //se crea construnctor con 6 parametros(no uso el AllArgs de lombok)
    public DetalleFactura(Factura factura, Long viajeId, BigDecimal tarifaBase, BigDecimal tarifaExtra, long tiempoUso, long tiempoPausado) {
        this.factura = factura;
        this.viajeId = viajeId;
        this.tarifaBase = tarifaBase;
        this.tarifaExtra = tarifaExtra;
        this.tiempoUso = tiempoUso;
        this.tiempoPausado = tiempoPausado;
    }

    public void calcularMonto(String tipoCuenta, Double kmAcumuladosMes) {
        BigDecimal multiplicadorDescuento = BigDecimal.ONE;

        if (tipoCuenta.equals("PREMIUM")) {
            if (kmAcumuladosMes < 100.0) {
                multiplicadorDescuento = BigDecimal.valueOf(0.5);
            } else {
                multiplicadorDescuento = BigDecimal.valueOf(0.5);
            }
        }

        BigDecimal tiempoUsoBD = BigDecimal.valueOf(tiempoUso);
        BigDecimal costoBase = tarifaBase.multiply(tiempoUsoBD).multiply(multiplicadorDescuento);
        BigDecimal costoExtra = BigDecimal.ZERO;

        if (tiempoPausado > 15) {
            BigDecimal tiempoPausadoExtra = BigDecimal.valueOf(tiempoPausado - 15);

            costoExtra = tarifaExtra.multiply(tiempoPausadoExtra);
        }

        this.montoCalculado = costoBase.add(costoExtra);
    }

}
