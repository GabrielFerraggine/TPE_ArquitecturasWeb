package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pausa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viaje_id", nullable = false)
    private Viaje viaje;

    public Duration getDuracion() {
        if (fechaHoraFin != null) {
            return Duration.between(fechaHoraInicio, fechaHoraFin);
        } else {
            return Duration.between(fechaHoraInicio, LocalDateTime.now());
        }
    }

    public boolean esPausaExtensa() {
        return getDuracion().toMinutes() > 15;
    }
}
