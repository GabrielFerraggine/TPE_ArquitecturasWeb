package controller;

import DTO.MonopatinDTO;
import entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import service.ServiceMonopatin;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/monopatin")
public class ControllerMonopatin {

    @Autowired
    private ServiceMonopatin serviceMonopatin;

    @GetMapping("")
    public ResponseEntity<List<MonopatinDTO>> traerTodosLosMonopatines() {
        List<MonopatinDTO> monopatines = serviceMonopatin.traerTodos();
        if (monopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monopatines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonopatinDTO> buscarMonopatinPorId(@PathVariable int id) {
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

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody MonopatinDTO mpDTO) {
        try {
            serviceMonopatin.save(mpDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Monopatin creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el Monopatin: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{estado}")
    public ResponseEntity<String> setEstado(@PathVariable("id") int idMonopatin, @PathVariable("estado") String estado, @RequestBody Monopatin monopatin) {
        if (estado.equals("enMantenimiento") || estado.equals("enUso") || (estado.equals("libre"))) {
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