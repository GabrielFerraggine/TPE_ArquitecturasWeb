package Controlador;

import DTO.DTOUsuario;
import Modelos.*;
import Entidades.Usuario;
import Servicio.ServicioUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class ControllerUsuario {

    private final ServicioUsuario servicioUsuario;

    public ControllerUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }
    /*==============================Llamadas de los FeignClient==============================*/
    /*g. Como usuario quiero un listado de los monopatines cercanos a mi zona,
    para poder encontrar un monopatín cerca de mi ubicación*/
    @GetMapping("/obtenerMonopatinesCercanos/{latitud}/{longitud}")
    public ResponseEntity<List<Monopatin>> obtenerMonopatinesCercanos(@PathVariable double latitud, @PathVariable double longitud) {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerMonopatinesCercanos(latitud, longitud));
        } catch (Exception e) {
            return  ResponseEntity.notFound().build();
        }
    }

    //TODO
    // /*h. Como usuario quiero saber cuánto he usado los monopatines en un período,
    //  y opcionalmente si otros usuarios relacionados a mi cuenta los han usado*/
    // --No se a que servicio pedirlo
    /*@GetMapping("/tipoUsoMonopatines/{idUsuario}")*/


    @PutMapping("/{dni}/anularCuentas")
    public ResponseEntity<String> anularCuentas(@PathVariable String dni) {
        try {
            return ResponseEntity.ok(servicioUsuario.anularCuentas(dni));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{dni}/activarCuentas")
    public ResponseEntity<String> activarCuenta(@PathVariable String dni) {
        try {
            return ResponseEntity.ok(servicioUsuario.activarCuentas(dni));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.*/
    @GetMapping("/monopatinesMasUsados/{anio}/{cantidadMinimaViajes}")
    public ResponseEntity<List<Long>> obtenerMonopatinesMasUsados(@PathVariable int anio,
                                                                  @PathVariable Long cantidadMinimaViajes) {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerMonopatinesMasUsados(anio, cantidadMinimaViajes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /*=============================Llamadas de usuario=====================================*/
    @GetMapping("/obtenerUsuario/{idUsuario}")
    public ResponseEntity<DTOUsuario> obtenerUsuario(@PathVariable String idUsuario) {  // Agregar @PathVariable
        try {
            if (servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.obtenerUsuario(idUsuario));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/obtenerUsuarios")
    public ResponseEntity<List<DTOUsuario>> obtenerUsuarios() {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerUsuarios());
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/agregarUsuario")
    public ResponseEntity<DTOUsuario> agregarUsuario(@RequestBody Usuario u) {  // Cambiar a @RequestBody
        try {
            if(!servicioUsuario.existeUsuario(u.getIdUsuario())) {
                DTOUsuario dtoUsuario = servicioUsuario.agregarUsuario(u);
                return ResponseEntity.status(HttpStatus.CREATED).body(dtoUsuario);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminarUsuario/{idUsuario}")
    public ResponseEntity<Boolean> eliminarUsuario(@PathVariable String idUsuario) {  // Agregar @PathVariable
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.eliminarUsuario(idUsuario));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/actualizarUsuario/{idUsuario}")
    public ResponseEntity<DTOUsuario> actualizarUsuario(@PathVariable String idUsuario, @RequestBody Usuario u) {  // Agregar @PathVariable y @RequestBody
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.actualizarUsuario(idUsuario, u));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}


