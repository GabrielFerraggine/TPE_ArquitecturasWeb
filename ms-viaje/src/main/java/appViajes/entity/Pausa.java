package appViajes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    public Pausa(LocalDateTime fechaHoraInicio, Long viajeId) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.viajeId = viajeId;
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

    public static LocalDateTime parsearFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(fechaStr, formatter);
        } catch (Exception e) {
            try {
                return LocalDateTime.parse(fechaStr);
            } catch (Exception ex) {
                throw new RuntimeException("Error al parsear fecha: " + fechaStr, ex);
            }
        }
    }

}
