package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idMonopatin;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private Long idCuenta;

    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;
    private Double kmRecorridos;
    private Double taifa;

    @Column(nullable = false)
    private Long paradaInicio;

    private Long paradaFinal;

    @Enumerated(EnumType.STRING)
    private EstadoViaje estado;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "viaje_id")
    private List<Pausa> pausas = new ArrayList<>();

    public Viaje(Long idMonopatin, Long idUsuario, Long idCuenta, LocalDateTime fechaHoraInicio, Long paradaInicio) {
        this.idMonopatin = idMonopatin;
        this.idUsuario = idUsuario;
        this.idCuenta = idCuenta;
        this.fechaHoraInicio = fechaHoraInicio;
        this.paradaInicio = paradaInicio;
        this.estado = EstadoViaje.EN_CURSO;
        this.pausas = new ArrayList<>();
        this.kmRecorridos = 0.0;
    }
    public void finalizarViaje(LocalDateTime fechaHoraFin, Long paradaFinal, Double kmRecorridos) {
        this.fechaHoraFin = fechaHoraFin;
        this.paradaFinal = paradaFinal;
        this.kmRecorridos = kmRecorridos;
        this.estado = EstadoViaje.FINALIZADO;
    }
    public void agregarPausa(Pausa pausa) {
        this.pausas.add(pausa);
    }

    public enum EstadoViaje {
        EN_CURSO,
        PAUSADO,
        FINALIZADO
    }
}
