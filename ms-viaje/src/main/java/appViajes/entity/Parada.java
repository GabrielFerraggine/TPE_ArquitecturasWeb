package appViajes.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "paradas")
@Data
public class Parada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Cambia esto
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false)
    private Boolean activa = true;

    private Double radioPermitidoMetros = 50.0;

}
