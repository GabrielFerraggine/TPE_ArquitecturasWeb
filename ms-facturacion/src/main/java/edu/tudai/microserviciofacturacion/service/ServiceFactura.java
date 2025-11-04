package edu.tudai.microserviciofacturacion.service;

import edu.tudai.microserviciofacturacion.entity.Factura;
import edu.tudai.microserviciofacturacion.repository.RepositoryFactura;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ServiceFactura {

    private final RepositoryFactura repositoryFactura;

    @Transactional
    public List<Factura> buscarTodas(){
        return repositoryFactura.findAll();
    }

    @Transactional
    public Factura buscarPorId(Long id){
        return repositoryFactura.findById(id).orElse(null);
    }

    @Transactional
    public Factura agregarFactura(Factura factura){
        return repositoryFactura.save(factura);
    }

    @Transactional
    public Factura actualizarFactura(Factura factura){
        return repositoryFactura.save(factura);
    }

    @Transactional
    public void eliminarFactura(Long id){
        repositoryFactura.deleteById(id);
    }

    public Double obtenerTotalFacturado(int anio, int mesInicio, int mesFin){
        return repositoryFactura.obtenerTotalFacturado(anio, mesInicio, mesFin);
    }
}
