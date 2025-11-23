package Aplicacion.entity;

import Aplicacion.DTO.MonopatinDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "monopatines")
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {

    @Id
    private Long idMonopatin;

    @Field
    private Estado estado; // enUso, enMantenimiento, libre

    @Field
    private double latitud;

    @Field
    private double longitud;

    @Field
    private double kmRecorridos;

    @Field
    private int tiempoDeUsoTotal;

    @Field
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
