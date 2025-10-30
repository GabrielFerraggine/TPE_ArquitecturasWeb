package controller;

import entity.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ServiceCuenta;

@RestController
@RequestMapping("/cuenta")
public class ControllerCuenta {

    @Autowired
    private ServiceCuenta serviceCuenta;

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> buscarCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serviceCuenta.buscarCuentaPorId(id));
    }
}
