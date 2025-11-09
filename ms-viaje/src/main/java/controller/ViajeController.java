package controller;

import dto.*;
import dto.IniciarViajeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ViajeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarVIaje(@RequestBody IniciarViajeRequest request) {
        try {
            ViajeDTO viaje = viajeService.iniciarViaje(request);
            return ResponseEntity.ok(viaje);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarViaje(@RequestBody FinalizarViajeRequest request) {
        try {
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





}
