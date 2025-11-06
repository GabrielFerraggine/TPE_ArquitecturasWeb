package controller;

import entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import service.ServiceMonopatin;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/monopatin")
public class ControllerMonopatin {

    @Autowired
    private ServiceMonopatin serviceMonopatin;

    @GetMapping("/{id}")
    public ResponseEntity<Monopatin> buscarMonopatinPorId(@PathVariable int id) {
        Monopatin mp = serviceMonopatin.buscarMonopatinPorId(id);
        if (mp == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mp);
    }

    @PutMapping("/{id}/{estado}")
    public ResponseEntity<String> setEstado(@PathVariable("id") int idMonopatin, @PathVariable("estado") String estado, @RequestBody Monopatin monopatin) {
        if (estado.equals("activar") || estado.equals("desactivar")) {
            if (serviceMonopatin.setEstado(idMonopatin, estado)) {
                return ResponseEntity.ok().body("El estado del Monopatin (id = " + idMonopatin + ") se ha actualizado a '" + estado + "'");
            }
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.badRequest().build();
    }

    public boolean esParadaPermitida() {
        //Algo con el GPS, Latitud y Longitud
        return true;
    }

    public void compenzarPausa() {

    }

    public void finalizarPausa() {

    }

}