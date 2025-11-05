package edu.tudai.microserviciofacturacion.controller;

import edu.tudai.microserviciofacturacion.DTO.DetalleFacturaDTO;
import edu.tudai.microserviciofacturacion.entity.DetalleFactura;
import edu.tudai.microserviciofacturacion.entity.Factura;
import edu.tudai.microserviciofacturacion.service.ServiceDetalleFactura;
import edu.tudai.microserviciofacturacion.service.ServiceFactura;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@Controller("/api/detalleFactura")
public class ControllerDetalleFactura {

    private final ServiceDetalleFactura serviceDetalleFactura;
    private final ServiceFactura serviceFactura;

    @GetMapping
    public ResponseEntity<List<DetalleFactura>> obtenerDetalleFactura() {
        List<DetalleFactura> detalleFacturas = serviceDetalleFactura.buscarTodas();
        if (detalleFacturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(detalleFacturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleFactura> obtenerDetalleFacturaPorId(@PathVariable("id") Long id){
        DetalleFactura detalleFactura = serviceDetalleFactura.buscarPorId(id);
        if (detalleFactura == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(detalleFactura);
    }

    @PostMapping
    public ResponseEntity<DetalleFactura> crearDetalleFactura(@RequestBody DetalleFacturaDTO detalleDTO){
        Factura factura = serviceFactura.buscarPorId(detalleDTO.getFacturaId());

        DetalleFactura detalle = new DetalleFactura(factura, detalleDTO.getViajeId(),
                detalleDTO.getTarifaBase(), detalleDTO.getTarifaExtra(),
                detalleDTO.getTiempoUso(), detalleDTO.getTiempoPausado());

        detalle.calcularMonto(detalleDTO.getTipoCuenta(), detalleDTO.getKmAcumuladosMes());  // calcular el monto antes de guardar
        factura.agregarDetalle(detalle); //este metodo llama a recalcularMontoTotal()
        serviceFactura.actualizarFactura(factura); // Gracias a CascadeType.ALL, esto guarda el detalle Y actualiza la factura
        return ResponseEntity.ok(detalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleFactura> actualizarDetalleFactura(@PathVariable("id") Long id, @RequestBody DetalleFactura detalleFactura) {
        DetalleFactura detalleFacturaExistente = serviceDetalleFactura.buscarPorId(id);

        if (detalleFacturaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        detalleFacturaExistente.setFactura(detalleFactura.getFactura());
        detalleFacturaExistente.setTiempoUso(detalleFactura.getTiempoUso());
        detalleFacturaExistente.setTiempoPausado(detalleFactura.getTiempoPausado());
        detalleFacturaExistente.setTarifaBase(detalleFactura.getTarifaBase());
        detalleFacturaExistente.setTarifaExtra(detalleFactura.getTarifaExtra());
        detalleFacturaExistente.setViajeId(detalleFactura.getViajeId());

        DetalleFactura detalleFacturaActualizada = serviceDetalleFactura.actualizarDetalleFactura(detalleFacturaExistente);

        return ResponseEntity.ok(detalleFacturaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleFactura> eliminarDetalleFactura(@PathVariable("id") Long id){
        serviceDetalleFactura.eliminarDetalleFactura(id);
        return ResponseEntity.noContent().build();
    }

}
