package edu.tudai.microserviciofacturacion.service;

import edu.tudai.microserviciofacturacion.entity.Tarifa;
import edu.tudai.microserviciofacturacion.repository.RepositoryTarifa;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Service
public class ServiceTarifa {

    private final RepositoryTarifa repositoryTarifa;

    @Transactional
    public List<Tarifa> buscarTodas(){
        return repositoryTarifa.findAll();
    }

    @Transactional
    public Tarifa buscarPorId(Long id){
        return repositoryTarifa.findById(id).orElse(null);
    }

    @Transactional
    public Tarifa agregarTarifa(Tarifa tarifa){
        return repositoryTarifa.save(tarifa);
    }

    @Transactional
    public Tarifa actualizarTarifa(Tarifa tarifa){
        return repositoryTarifa.save(tarifa);
    }

    @Transactional
    public void eliminarTarifa(Long id){
        repositoryTarifa.deleteById(id);
    }

    @Transactional
    public void ajustarPrecios(BigDecimal nuevaTarifaBase, BigDecimal nuevaTarifaExtra, LocalDate fechaInicio){

        //creo tarifa vacio y le asigno los valores
        Tarifa tarifaBase =  new Tarifa();
        tarifaBase.setTipo(Tarifa.TipoTarifa.BASE);
        tarifaBase.setMonto(nuevaTarifaBase);
        tarifaBase.setFechaInicio(fechaInicio);
        tarifaBase.setFechaFin(null);

        Tarifa tarifaExtra =  new Tarifa();
        tarifaExtra.setTipo(Tarifa.TipoTarifa.EXTRA_PAUSA);
        tarifaExtra.setMonto(nuevaTarifaExtra);
        tarifaExtra.setFechaInicio(fechaInicio);
        tarifaExtra.setFechaFin(null);

        repositoryTarifa.save(tarifaBase);
        repositoryTarifa.save(tarifaExtra);

    }
}
