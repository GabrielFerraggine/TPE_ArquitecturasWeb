package controller;

import dtos.CuentaResponseDTO;
import dtos.SaldoRequestDTO;
import entity.Cuenta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ServiceCuenta;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class ControllerCuenta {

    @Autowired
    private ServiceCuenta serviceCuenta;

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> buscarCuentaPorId(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(serviceCuenta.buscarCuentaPorId(id));
    }

    //Post - crearCuenta
    @PostMapping("")
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@Valid @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(serviceCuenta.crearCuenta(cuenta));
    }

    //Get - buscarTodasLasCuentas
    @GetMapping("")
    public ResponseEntity<List<CuentaResponseDTO>> buscarTodasLasCuentas() {
        return ResponseEntity.ok(serviceCuenta.buscarTodasLasCuentas());
    }

    //Get - buscarCuentasPorDNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<CuentaResponseDTO>> buscarCuentasPorDNI(@PathVariable @NotNull String dni) {
        return ResponseEntity.ok(serviceCuenta.buscarCuentasPorDNI(dni));
    }


    //Put - anularCuenta
    @PutMapping("{id}/anular")
    public ResponseEntity<String> anularCuenta(@PathVariable @NotNull Long id) {
        try {
            serviceCuenta.anularCuenta(id);
            return ResponseEntity.ok("Cuenta anulada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo anular la cuenta: " + e.getMessage());
        }
    }
    //Put - activarCuenta
    @PutMapping("{id}/activar")
    public ResponseEntity<String> activarCuenta(@PathVariable @NotNull Long id) {
        try {
            serviceCuenta.activarCuenta(id);
            return ResponseEntity.ok("Cuenta activada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo activar la cuenta: " + e.getMessage());
        }
    }

    //Post - depositarSaldo
    @PostMapping("{id}/depositar")
    public ResponseEntity<String> depositarSaldo(@PathVariable @NotNull Long id, @Valid @RequestBody SaldoRequestDTO saldo) {
        try {
            serviceCuenta.depositarSaldo(id, saldo);
            return ResponseEntity.ok("Saldo depositado con exito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo depositar el saldo: " + e.getMessage());
        }
    }

    //Post - extraerSaldo
    @PostMapping("{id}/extraer")
    public ResponseEntity<String> extraerSaldo(@PathVariable @NotNull Long id, @Valid @RequestBody SaldoRequestDTO saldo) {
        try {
            serviceCuenta.extraerSaldo(id, saldo);
            return ResponseEntity.ok("Saldo extraido con exito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo extraer el saldo: " + e.getMessage());
        }
    }

    //Get - verSaldo
    @GetMapping("{id}/saldo")
    public ResponseEntity<BigDecimal> verSaldo(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(serviceCuenta.verSaldo(id));
    }



}
