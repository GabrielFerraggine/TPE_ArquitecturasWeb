package Aplicacion.controller;

import Aplicacion.DTO.DetalleFacturaDTO;
import Aplicacion.entity.DetalleFactura;
import Aplicacion.entity.Factura;
import Aplicacion.service.ServiceDetalleFactura;
import Aplicacion.service.ServiceFactura;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/detalleFactura")
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

        detalle.calcularMonto(detalleDTO.getTipoCuenta(), detalleDTO.getKmAcumuladosMes());  // calcular el monto


        DetalleFactura detalleGuardado = serviceDetalleFactura.agregarDetalleFactura(detalle);
        factura.agregarDetalle(detalleGuardado); // este metodo llama a recalcularMontoTotal()
        serviceFactura.actualizarFactura(factura);

        return ResponseEntity.ok(detalleGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleFactura> actualizarDetalleFactura(@PathVariable("id") Long id, @RequestBody DetalleFacturaDTO detalleDTO) {
        DetalleFactura detalleFacturaExistente = serviceDetalleFactura.buscarPorId(id);

        if (detalleFacturaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        Factura factura = serviceFactura.buscarPorId(detalleDTO.getFacturaId());
        if (factura == null) {
            return ResponseEntity.badRequest().build();
        }

        detalleFacturaExistente.setFactura(factura);
        detalleFacturaExistente.setViajeId(detalleDTO.getViajeId());
        detalleFacturaExistente.setTiempoUso(detalleDTO.getTiempoUso());
        detalleFacturaExistente.setTiempoPausado(detalleDTO.getTiempoPausado());
        detalleFacturaExistente.setTarifaBase(detalleDTO.getTarifaBase());
        detalleFacturaExistente.setTarifaExtra(detalleDTO.getTarifaExtra());

        detalleFacturaExistente.calcularMonto(detalleDTO.getTipoCuenta(), detalleDTO.getKmAcumuladosMes());

        DetalleFactura detalleFacturaActualizada = serviceDetalleFactura.actualizarDetalleFactura(detalleFacturaExistente);

        factura.recalcularMontoTotal();
        serviceFactura.actualizarFactura(factura);

        return ResponseEntity.ok(detalleFacturaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleFactura> eliminarDetalleFactura(@PathVariable("id") Long id){
        serviceDetalleFactura.eliminarDetalleFactura(id);
        return ResponseEntity.noContent().build();
    }

}
