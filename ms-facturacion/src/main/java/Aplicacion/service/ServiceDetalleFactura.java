package Aplicacion.service;

import Aplicacion.entity.DetalleFactura;
import Aplicacion.repository.RepositoryDetalleFactura;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class ServiceDetalleFactura {

    private final RepositoryDetalleFactura repositoryDetalleFactura;

    @Transactional
    public List<DetalleFactura> buscarTodas(){
        return repositoryDetalleFactura.findAll();
    }

    @Transactional
    public DetalleFactura buscarPorId(Long id){
        return repositoryDetalleFactura.findById(id).orElse(null);
    }

    @Transactional
    public DetalleFactura agregarDetalleFactura(DetalleFactura detalleFactura){
        return repositoryDetalleFactura.save(detalleFactura);
    }

    public DetalleFactura actualizarDetalleFactura(DetalleFactura detalleFactura){
        return repositoryDetalleFactura.save(detalleFactura);
    }

    public void eliminarDetalleFactura(Long id){
        repositoryDetalleFactura.deleteById(id);
    }
}
