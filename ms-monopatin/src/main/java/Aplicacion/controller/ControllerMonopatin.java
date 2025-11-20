package Aplicacion.controller;

import Aplicacion.DTO.MonopatinDTO;
import Aplicacion.DTO.ReporteDTO;
import Aplicacion.entity.Monopatin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Aplicacion.service.ServiceMonopatin;
import org.springframework.beans.factory.annotation.Autowired;
import Aplicacion.entity.Estado;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/monopatin")
public class ControllerMonopatin {

    @Autowired
    private ServiceMonopatin serviceMonopatin;

    @GetMapping("/")
    public ResponseEntity<List<MonopatinDTO>> traerTodosLosMonopatines() {
        List<MonopatinDTO> monopatines = serviceMonopatin.traerTodos();
        if (monopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monopatines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonopatinDTO> buscarMonopatinPorId(@PathVariable Long id) {
        Monopatin mp = serviceMonopatin.buscarMonopatinPorId(id);
        if (mp == null) {
            return  ResponseEntity.notFound().build();
        }
        MonopatinDTO mpDTO = new MonopatinDTO(mp.getIdMonopatin(),
                                              mp.getEstado(),
                                              mp.getLatitud(),
                                              mp.getLongitud(),
                                              mp.getKmRecorridos(),
                                              mp.getTiempoDeUsoTotal(),
                                              mp.getTiempoDePausas());
        return ResponseEntity.ok(mpDTO);
    }

    @GetMapping("/{id}/LIBRE")
    public ResponseEntity<Boolean> estaDisponible(@PathVariable Long id) {
        Estado estado = serviceMonopatin.getEstado(id);
        return ResponseEntity.ok().body(estado == Estado.LIBRE);
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody MonopatinDTO mpDTO) {
        try {
            serviceMonopatin.save(mpDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Monopatin creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el Monopatin: " + e.getMessage());
        }
    }

    // LIBRE, ENUSO, ENMANTENIMIENTO
    @PutMapping("/{id}/cambiarEstado/{estado}")
    public ResponseEntity<String> setEstado(@PathVariable("id") Long idMonopatin, @PathVariable("estado") Estado estado) {
        if (Estado.ENUSO == estado || Estado.ENMANTENIMIENTO == estado || Estado.LIBRE == estado) {
            if (serviceMonopatin.setEstado(idMonopatin, estado)) {
                return ResponseEntity.ok().body("El estado del Monopatin (id = " + idMonopatin + ") se ha actualizado a '" + estado + "'");
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("{id}/latitud")
    public ResponseEntity<Double> obtenerLatitud(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMonopatin.getLatitud(id));
    }

    @GetMapping("{id}/longitud")
    public ResponseEntity<Double> obtenerLongitud(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMonopatin.getLongitud(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        Monopatin mp = serviceMonopatin.buscarMonopatinPorId(id);
        if (mp == null) {
            return ResponseEntity.notFound().build();
        }
        serviceMonopatin.borrarMonopatin(mp);
        return ResponseEntity.ok().body("Se borró el Monopatin (id = " + id + ")");
    }

    /*reporte/kmRecorridos?tiempoDePausas=true & ... */
    @GetMapping("/reporte/kmRecorridos")
    public ResponseEntity<List<ReporteDTO>> getReportePorKmRecorridos(@RequestParam(required = false)Optional<Boolean> tiempoDePausas) {
        List<ReporteDTO> reporte = null;
        if (tiempoDePausas.isPresent()) {
            reporte = serviceMonopatin.getReportePorKmYTiempoDePausas();
        } else {
            reporte = serviceMonopatin.getReportePorKmRecorridos();
        }
        if (reporte != null) {
            return ResponseEntity.ok(reporte);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reporte/tiempoDeUsoTotal")
    public ResponseEntity<List<ReporteDTO>> getReportePorTiempoDeUsoTotal() {
        List<ReporteDTO> reporte = serviceMonopatin.getReportePorTiempoDeUsoTotal();
        if (reporte != null) {
            return ResponseEntity.ok(reporte);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reporte/tiempoDePausas")
    public ResponseEntity<List<ReporteDTO>> getReportePorTiempoDePausas() {
        List<ReporteDTO> reporte = serviceMonopatin.getReportePorTiempoDePausas();
        if (reporte != null) {
            return ResponseEntity.ok(reporte);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteDTO>> getReporteCompleto() {
        List<ReporteDTO> reporte = serviceMonopatin.getReporteCompleto();
        if (reporte != null) {
            return ResponseEntity.ok(reporte);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/finalizarRecorrido/{idMonopatin}/{kmRecorridos}/{tiempoDeUsoTotal}/{tiempoDePausas}")
    public ResponseEntity<?> finalizarRecorrido(@PathVariable("idMonopatin") Long idMonopatin, @PathVariable double kmRecorridos, @PathVariable int tiempoDeUsoTotal, @PathVariable int tiempoDePausas) {
        boolean resultado = serviceMonopatin.finalizarRecorrido(idMonopatin, kmRecorridos, tiempoDeUsoTotal, tiempoDePausas);
        if (resultado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().body("Error al finalizar el Monopatin");
    }

    @GetMapping("/obtenerMonopatinesCercanos/{latitud}/{longitud}")
    public ResponseEntity<List<MonopatinDTO>> getMonopatinesCercanos(@PathVariable("latitud") Double latitud, @PathVariable("longitud") double longitud) {
        List<MonopatinDTO> monopatines = serviceMonopatin.getMonopatinesCercanos(latitud, longitud);
        if (monopatines != null) {
            return ResponseEntity.ok(monopatines);
        }
        return ResponseEntity.notFound().build();
    }

    /*
    /reporte
    /reporte/kmRecorridos
    /reporte/kmRecorridos?tiempoDePausas=true


    /{idMonopatin}/finalizarRecorrido




    */
}