package service;

import DTO.MonopatinDTO;
import entity.Monopatin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.RepositoryMonopatin;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ServiceMonopatin {

    @Autowired
    private RepositoryMonopatin repoMonopatin;

    @Transactional
    public Monopatin buscarMonopatinPorId(int idMonopatin) {
        return repoMonopatin.buscarPorId(idMonopatin);
    }

    @Transactional(readOnly = true)
    public List<MonopatinDTO> traerTodos() {
        List<Monopatin> monopatines = repoMonopatin.findAll();
        return monopatines.stream().map(MonopatinDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public boolean setEstado(int idMonopatin, String estado) {
        return (repoMonopatin.setEstado(idMonopatin, estado) == 1);
    }

    @Transactional
    public MonopatinDTO save(MonopatinDTO monopatinDto) {
        Monopatin monopatin = new Monopatin(monopatinDto);
        repoMonopatin.save(monopatin);
        return monopatinDto;
    }

    public boolean esParadaPermitida() {
        //Algo con el GPS, Latitud y Longitud
        return true;
    }

    public void compenzarPausa() {

    }

    public void finalizarPausa() {

    }

    /*
    public ArrayList<Double> getUbicacion() {
        ArrayList<Double> ubicacion = new ArrayList<Double>();
        ubicacion.addFirst(this.latitud);
        ubicacion.addLast(this.longitud);
        return ubicacion;
    }
    */

    //Sumar Tiempos Y KMs recorridos?

    //Generar Reporte? Retornar una lista o un JSON/XML ?

    // Ubicar Monopatin? Retornar Ubicacion Actual?

    /*
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

    */
}

