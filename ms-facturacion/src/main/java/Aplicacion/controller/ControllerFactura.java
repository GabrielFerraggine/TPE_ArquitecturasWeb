package Aplicacion.controller;

import Aplicacion.entity.Factura;
import Aplicacion.service.ServiceFactura;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Data
@RequestMapping("/api/factura")
public class ControllerFactura {

    private final ServiceFactura serviceFactura;

    @GetMapping
    public ResponseEntity<List<Factura>> obtenerTodas(){
        List<Factura> facturas = serviceFactura.buscarTodas();
        if(facturas.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable("id") Long id){
        Factura factura = serviceFactura.buscarPorId(id);
        if(factura == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(factura);
    }

    @PostMapping
    public ResponseEntity<Factura> insertarFactura(@RequestBody Factura factura){
        Factura facturaNueva = serviceFactura.agregarFactura(factura);
        if(facturaNueva == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(facturaNueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizarFactura(@PathVariable("id") Long id, @RequestBody Factura factura) {
        Factura facturaExistente = serviceFactura.buscarPorId(id);

        if (facturaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        facturaExistente.setUsuarioId(factura.getUsuarioId());
        facturaExistente.setFechaEmision(factura.getFechaEmision());

        Factura facturaUpdated = serviceFactura.actualizarFactura(facturaExistente);

        return ResponseEntity.ok(facturaUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable("id") Long id){
        serviceFactura.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totalFacturado/{anio}/{mesInicio}/{mesFin}")
    public ResponseEntity<Double> obtenerTotalFacturado(
            @PathVariable int anio, @PathVariable int mesInicio, @PathVariable int mesFin){

        Double totalFacturado = serviceFactura.obtenerTotalFacturado(anio, mesInicio, mesFin);

        if (totalFacturado == null) {
            totalFacturado = 0.0;
        }

        System.out.println("Total calculado: " + totalFacturado);
        return ResponseEntity.ok(totalFacturado);
    }
}
