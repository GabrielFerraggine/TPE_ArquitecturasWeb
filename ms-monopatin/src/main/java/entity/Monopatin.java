package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
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

    public void activarMonopatin() {
        //this.estado = "";
    }

    public void desactivarMonopatin() {

    }

    public boolean esParadaPermitida() {
        //Algo con el GPS, Latitud y Longitud
        return true;
    }

    public void compenzarPausa() {

    }

    public void finalizarPausa() {

    }

    public ArrayList<Double> getUbicacion() {
        ArrayList<Double> ubicacion = new ArrayList<Double>();
        ubicacion.addFirst(this.latitud);
        ubicacion.addLast(this.longitud);
        return ubicacion;
    }

    //Sumar Tiempos Y KMs recorridos?

    //Generar Reporte? Retornar una lista o un JSON/XML ?

    // Ubicar Monopatin? Retornar Ubicacion Actual?

    @Override
    public void setEstado(String nuevoEstado) {
        if (nuevoEstado.equals("enUso")) {
            this.estado = "Activo";
        } else if (nuevoEstado.equals("enMantenimiento")) {
            this.estado = "enMantenimiento";
        } else if (nuevoEstado.equals("libre")) {
            this.estado = "libre";
        } else {
            // Devolver Un error
        }
    }
}
