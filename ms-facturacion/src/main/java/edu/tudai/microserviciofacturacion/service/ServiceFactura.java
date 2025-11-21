package edu.tudai.microserviciofacturacion.service;

import edu.tudai.microserviciofacturacion.entity.Factura;
import edu.tudai.microserviciofacturacion.repository.RepositoryFactura;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Data
public class ServiceFactura {

    private static final Logger log = LoggerFactory.getLogger(ServiceFactura.class);
    private final RepositoryFactura repositoryFactura;

    @Transactional
    public List<Factura> buscarTodas() {
        return repositoryFactura.findAll();
    }

    @Transactional
    public Factura buscarPorId(Long id) {
        return repositoryFactura.findById(id).orElse(null);
    }

    @Transactional
    public Factura agregarFactura(Factura factura) {
        log.info("[MOCK MERCADOPAGO] Procesando pago...");
        return repositoryFactura.save(factura);
    }

    @Transactional
    public Factura actualizarFactura(Factura factura) {
        return repositoryFactura.save(factura);
    }

    @Transactional
    public void eliminarFactura(Long id) {
        repositoryFactura.deleteById(id);
    }

    @Transactional
    public Double obtenerTotalFacturado(int anio, int mesInicio, int mesFin) {
        return repositoryFactura.obtenerTotalFacturado(anio, mesInicio, mesFin);
    }
}
