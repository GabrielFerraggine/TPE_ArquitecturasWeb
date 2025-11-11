package edu.tudai.microserviciofacturacion.controller;

import edu.tudai.microserviciofacturacion.entity.Tarifa;
import edu.tudai.microserviciofacturacion.service.ServiceTarifa;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Controller
@RequestMapping("/api/tarifa")
public class ControllerTarifa {
    private final ServiceTarifa serviceTarifa;

    @GetMapping
    public ResponseEntity<List<Tarifa>> obtenerTodas() {
        List<Tarifa> tarifas = serviceTarifa.buscarTodas();
        if (tarifas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> obtenerPorId(@PathVariable("id") Long id) {
        Tarifa tarifa = serviceTarifa.buscarPorId(id);
        if (tarifa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarifa);
    }

    @PostMapping
    public ResponseEntity<Tarifa> insertarTarifa(@RequestBody Tarifa tarifa) {
        Tarifa tarifaNueva = serviceTarifa.agregarTarifa(tarifa);
        if (tarifaNueva == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tarifaNueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> actualizarTarifa(@PathVariable("id") Long id, @RequestBody Tarifa tarifa) {
        Tarifa tarifaExistente = serviceTarifa.buscarPorId(id);
        if (tarifaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        tarifaExistente.setTipo(tarifa.getTipo());
        tarifaExistente.setFechaInicio(tarifa.getFechaInicio());
        tarifaExistente.setFechaFin(tarifa.getFechaFin());
        tarifaExistente.setMonto(tarifa.getMonto());

        Tarifa tarifaActualizada = serviceTarifa.actualizarTarifa(tarifaExistente);

        return ResponseEntity.ok(tarifaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tarifa> eliminarTarifa(@PathVariable("id") Long id) {
        serviceTarifa.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ajustarPrecios/{nuevaTarifaBase}/{nuevaTarifaExtra}/{fechaInicio}")
    public ResponseEntity<Void> ajustarPreciosTarifas(@PathVariable BigDecimal nuevaTarifaBase,
                                                      @PathVariable BigDecimal nuevaTarifaExtra,
                                                      @PathVariable LocalDate fechaInicio) {

        serviceTarifa.ajustarPrecios(nuevaTarifaBase, nuevaTarifaExtra, fechaInicio);
        return ResponseEntity.ok().build();
    }
}
