package controller;

import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ServiceMonopatin;

@RestController
@RequestMapping("/monopatin")
public class ControllerMonopatin {

    @Autowired
    private ServiceMonopatin serviceMonopatin;

    /*

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> buscarCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serviceCuenta.buscarCuentaPorId(id));
    }

     */
}