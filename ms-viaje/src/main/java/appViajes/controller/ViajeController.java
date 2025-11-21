package appViajes.controller;

import appViajes.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import appViajes.service.ParadaService;
import appViajes.service.ViajeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private ParadaService paradaService;

    @GetMapping("/")
    public ResponseEntity<List<ViajeDTO>> reportarViajes() {
        List<ViajeDTO> viajes = viajeService.reportarViajes();
        if (viajes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(viajes);
    }

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarViaje(@RequestBody IniciarViajeRequest request) {
        try {
            ViajeDTO viaje = viajeService.iniciarViaje(request);
            return ResponseEntity.ok(viaje);

        } catch (RuntimeException e) {
            System.err.println("Error en controller: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Opcional: mantener el endpoint con path variables
    @PostMapping("/iniciar/{idMonopatin}/{idUsuario}/{idCuenta}/{idParadaInicio}/{idParadaFinal}")
    public ResponseEntity<?> iniciarViajeConPath(
            @PathVariable Long idMonopatin,
            @PathVariable Long idUsuario,
            @PathVariable Long idCuenta,
            @PathVariable Long idParadaInicio,
            @PathVariable Long idParadaFinal) {

        IniciarViajeRequest request = new IniciarViajeRequest();
        request.setIdMonopatin(idMonopatin);
        request.setIdUsuario(idUsuario);
        request.setIdCuenta(idCuenta);
        request.setParadaInicio(idParadaInicio);
        request.setParadaFinal(idParadaFinal);

        return iniciarViaje(request);
    }



    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarViaje(@RequestBody FinalizarViajeRequest request) {

        try {
            if (request.getLatitud() == null || request.getLongitud() == null) {
                return ResponseEntity.badRequest().body("Latitud e Longitud invalido");
            }

            if (!paradaService.paradaValida(request.getLatitud(), request.getLongitud())) {
                return ResponseEntity.badRequest().body("No se encuentra en una parada válida");
            }

            ViajeDTO viaje = viajeService.finalizarViaje(request);
            return ResponseEntity.ok(viaje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/pausar")
    public ResponseEntity<?> pausarViaje(@RequestBody PausaRequest request) {
        try {
            ViajeDTO viaje = viajeService.pausarViaje(request);
            return ResponseEntity.ok(viaje);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reanudar")
    public ResponseEntity<?> retomarViaje(@RequestBody PausaRequest request) {
        try {
            ViajeDTO viaje = viajeService.retomarViaje(request);
            return ResponseEntity.ok(viaje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ViajeDTO>> obtenerViajesPorUsuario(@PathVariable Long idUsuario) {
        List<ViajeDTO> viajes = viajeService.obtenerViajesPorUsuario(idUsuario);
        return ResponseEntity.ok(viajes);
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<ViajeDTO>> obtenerViajesPorCuenta(@PathVariable Long idCuenta) {
        List<ViajeDTO> viajes = viajeService.obtenerViajesPorCuenta(idCuenta);
        return ResponseEntity.ok(viajes);
    }

    /**/
    @GetMapping("/viajesFrecuentes/{cantidadMinima}/{anio}")
    public ResponseEntity<List<Map<String, Object>>> obtenerMonopatinesConMasDeXViajes(
            @RequestParam Long cantidadMinima,
            @RequestParam Integer anio) {

        try {
            List<Map<String, Object>> resultado = viajeService
                    .obtenerMonopatinesConMasDeXViajesEnAnio(cantidadMinima, anio);

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //Como administrador quiero ver los usuarios que más utilizan los monopatines, filtrado por período y por tipo de usuario.
    //Como usuario quiero saber cuánto he usado los monopatines en un período, y opcionalmente si otros usuarios relacionados a mi cuenta los han usado.

    //Datos enviados: idUsuario, inicio, fin, boolean verCuentasRelacionadas
    //Opcionalmente si otros usuarios relacionados a mi cuenta los han usado.
    //Datos respuesta: int tiempoUsoMonopatines

    // Endpoint alternativo con path variables para mayor flexibilidad
    @GetMapping("/tiempoUsoMonopatines/{idUsuario}/{fechaInicio}/{fechaFin}/{verCuentasRelacionadas}")
    public ResponseEntity<?> tiempoUsoMonopatinesPath(
            @PathVariable Long idUsuario,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @PathVariable Boolean verCuentasRelacionadas) {

        TiempoUsoRequest request = new TiempoUsoRequest();
        request.setIdUsuario(idUsuario);
        request.setFechaInicio(fechaInicio);
        request.setFechaFin(fechaFin);
        request.setVerCuentasRelacionadas(verCuentasRelacionadas);

        return tiempoUsoMonopatines(request);
    }

    @PostMapping("/tiempoUsoMonopatines")
    public ResponseEntity<?> tiempoUsoMonopatines(@RequestBody TiempoUsoRequest request) {
        try {
            TiempoUsoResponse response = viajeService.obtenerTiempoUsoMonopatines(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    // Endpoint para administradores - top usuarios por uso
    @GetMapping("/admin/topUsuarios")
    public ResponseEntity<?> obtenerTopUsuariosPorUso(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String tipoUsuario) {

        try {
            List<Map<String, Object>> topUsuarios = viajeService.obtenerTopUsuariosPorUso(fechaInicio, fechaFin, tipoUsuario);
            return ResponseEntity.ok(topUsuarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el reporte: " + e.getMessage());
        }
    }
    */

}
