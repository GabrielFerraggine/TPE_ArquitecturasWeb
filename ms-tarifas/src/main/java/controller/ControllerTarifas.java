package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.*;

@RestController
@RequestMapping("/tarifas")
public class ControllerTarifas {

    @Autowired
    private ServiceTarifas serviceTarifas;

    @PostMapping("/crear")
    public ResponseEntity<Tarifas> crearTarifa(@RequestBody Tarifas tarifa) {
        return new ResponseEntity<>(ServiceTarifas.crearTarifa(tarifa), HttpStatus.CREATED);
    }
}