package appViajes.controller;

import appViajes.dto.FinalizarViajeRequest;
import appViajes.dto.PausaRequest;
import appViajes.dto.ViajeDTO;
import appViajes.dto.IniciarViajeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import appViajes.service.ParadaService;
import appViajes.service.ViajeService;

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




}
