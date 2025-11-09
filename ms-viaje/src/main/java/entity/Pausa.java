package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "pausas")
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

    @Column(nullable = false)
    private Boolean pausaExtendida = false;

    public Pausa(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.pausaExtendida = false;
    }

    public void finalizarPausa(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
        Duration duracion = Duration.between(this.fechaHoraInicio, this.fechaHoraFin);
        this.pausaExtendida = duracion.toMinutes() > 15;
    }

    public Long getDuracionEnMinutos() {
        if (fechaHoraFin == null) {
            return 0L;
        }
        return Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes();
    }
}
