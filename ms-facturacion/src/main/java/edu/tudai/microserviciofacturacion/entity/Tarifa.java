package edu.tudai.microserviciofacturacion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
public class Tarifa {

    public enum TipoTarifa {
        BASE,
        EXTRA_PAUSA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTarifa tipo;

    private Double monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
