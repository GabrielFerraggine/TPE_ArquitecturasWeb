package appViajes.controller;

import appViajes.dto.ParadaDTO;
import appViajes.entity.Parada;
import appViajes.service.ParadaService;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paradas")
public class ParadaController {

    @Autowired
    private ParadaService paradaService;

    @PostMapping("/crearParada")
    public ResponseEntity<?> crearParada(@RequestBody ParadaDTO paradaDTO) {
        try {
            ParadaDTO nuevaParada = paradaService.crearParada(paradaDTO);
            return ResponseEntity.ok().body(nuevaParada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /*
    @PostMapping("/crearParada/{nombre}/{latitud}/{longitud}/{activa}/{radioPermitidoMetros}")
public ResponseEntity<?> crearParadaConPathVariables(
        @PathVariable String nombre,
        @PathVariable Double latitud,
        @PathVariable Double longitud,
        @PathVariable(required = false) Boolean activa,
        @PathVariable(required = false) Double radioPermitidoMetros) {

        try {
            // Crear DTO con los datos recibidos
            ParadaDTO paradaDTO = new ParadaDTO();
            paradaDTO.setNombre(nombre);
            paradaDTO.setLatitud(latitud);
            paradaDTO.setLongitud(longitud);
            paradaDTO.setActiva(activa != null ? activa : true);
            paradaDTO.setRadioPermitidoMetros(radioPermitidoMetros != null ? radioPermitidoMetros : 50.0);

            ParadaDTO nuevaParada = paradaService.crearParada(paradaDTO);
            return ResponseEntity.ok(nuevaParada);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

     */

    @GetMapping("/obtenerTodas")
    public ResponseEntity<List<ParadaDTO>> obtenerTodasParadas() {
        List<ParadaDTO> paradas = paradaService.obtenerTodasParadas();
        return ResponseEntity.ok().body(paradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerParadaPorId(@PathVariable Long id) {
        try {
            ParadaDTO parada = paradaService.obtenerParadaPorId(id);
            return ResponseEntity.ok(parada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/actualizarParada")
    public ResponseEntity<?> actualizarParada(@PathVariable Long id, @RequestBody ParadaDTO paradaDTO) {
        try {
            ParadaDTO paradaActualizada = paradaService.actualizarParada(id, paradaDTO);
            return ResponseEntity.ok(paradaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/borrar")
    public ResponseEntity<?> borrarParada(@PathVariable Long id) {
        try{
            paradaService.borrarParada(id);
            return ResponseEntity.ok().body("Parada borrada");
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
