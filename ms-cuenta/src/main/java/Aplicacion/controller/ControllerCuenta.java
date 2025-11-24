package Aplicacion.controller;

import Aplicacion.dtos.CuentaRequestDTO;
import Aplicacion.dtos.CuentaResponseDTO;
import Aplicacion.dtos.SaldoRequestDTO;
import Aplicacion.dtos.SaldoResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Aplicacion.service.ServiceCuenta;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class ControllerCuenta {

    @Autowired
    private ServiceCuenta serviceCuenta;

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> buscarCuentaPorId(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(serviceCuenta.buscarCuentaPorId(id));
    }

    @PostMapping("")
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@Valid @RequestBody CuentaRequestDTO cuenta) {
        return ResponseEntity.ok(serviceCuenta.crearCuenta(cuenta));
    }

    @GetMapping("")
    public ResponseEntity<List<CuentaResponseDTO>> buscarTodasLasCuentas() {
        return ResponseEntity.ok(serviceCuenta.buscarTodasLasCuentas());
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<CuentaResponseDTO>> buscarCuentasPorDNI(@PathVariable @NotNull String dni) {
        System.out.println("----------------obtenerUsuarios");
        return ResponseEntity.ok(serviceCuenta.buscarCuentasPorDNI(dni));
    }

    @PutMapping("{id}/anular")
    public ResponseEntity<String> anularCuenta(@PathVariable @NotNull Long id) {
        try {
            serviceCuenta.anularCuenta(id);
            return ResponseEntity.ok("Cuenta anulada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo anular la cuenta: " + e.getMessage());
        }
    }

    @PutMapping("{id}/activar")
    public ResponseEntity<String> activarCuenta(@PathVariable @NotNull Long id) {
        try {
            serviceCuenta.activarCuenta(id);
            return ResponseEntity.ok("Cuenta activada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo activar la cuenta: " + e.getMessage());
        }
    }

    @PostMapping("{id}/depositar")
    public ResponseEntity<String> depositarSaldo(@PathVariable @NotNull Long id, @Valid @RequestBody SaldoRequestDTO saldo) {
        try {
            serviceCuenta.depositarSaldo(id, saldo);
            return ResponseEntity.ok("Saldo depositado con exito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo depositar el saldo: " + e.getMessage());
        }
    }

    @PostMapping("{id}/extraer")
    public ResponseEntity<String> extraerSaldo(@PathVariable @NotNull Long id, @Valid @RequestBody SaldoRequestDTO saldo) {
        try {
            serviceCuenta.extraerSaldo(id, saldo);
            return ResponseEntity.ok("Saldo extraido con exito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo extraer el saldo: " + e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity<SaldoResponseDTO> verSaldo(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(serviceCuenta.verSaldo(id));
    }

    @GetMapping("/{id}/premium")
    public boolean verificarCuentaPremium(@PathVariable @NotNull Long id) {
        return serviceCuenta.verificarCuentaPremium(id);
    }



}
