package entity;

import DTO.MonopatinDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMonopatin;
    private String estado; // enUso, enMantenimiento, libre
    private double latitud;
    private double longitud;
    private double kmRecorridos;
    private int tiempoDeUsoTotal;
    private int tiempoDePausas;

    public Monopatin(MonopatinDTO mpDTO) {
        this.idMonopatin = mpDTO.getIdMonopatin();
        this.estado = mpDTO.getEstado();
        this.latitud = mpDTO.getLatitud();
        this.longitud = mpDTO.getLongitud();
        this.kmRecorridos = mpDTO.getKmRecorridos();
        this.tiempoDeUsoTotal = getTiempoDeUsoTotal();
        this.tiempoDePausas = getTiempoDePausas();
    }
}
