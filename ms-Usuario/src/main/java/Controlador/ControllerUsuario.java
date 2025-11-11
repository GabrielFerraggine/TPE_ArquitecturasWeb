package Controlador;

import DTO.DTOUsuario;
import Modelos.*;
import Entidades.Usuario;
import Servicio.ServicioUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    /*@GetMapping("/obtenerMonopatinesCercanos/{latitud}/{longitud}")
    public ResponseEntity<List<Monopatin>> obtenerMonopatinesCercanos(@PathVariable int latitud, @PathVariable int longitud) {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerMonopatinesCercanos(latitud, longitud));
        } catch (Exception e) {
            return  ResponseEntity.notFound().build();
        }
    }*/

    //TODO
    // /*h. Como usuario quiero saber cuánto he usado los monopatines en un período,
    //  y opcionalmente si otros usuarios relacionados a mi cuenta los han usado*/
    // --No se a que servicio pedirlo

    /*@GetMapping("/obtenerCuentaUsuario/{idUsuario}")
    public ResponseEntity<Cuenta> obtenerCuentaUsuario(@PathVariable String idUsuario) {
        try {
            System.out.println("HOLAAAAAAAAAA idUsuario: " + idUsuario);
            System.out.println(servicioUsuario.obtenerCuentaUsuario(idUsuario) + "UwU");
            return ResponseEntity.ok(servicioUsuario.obtenerCuentaUsuario(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/


    @GetMapping("/obtenerCuentasUsuarios/{idCuenta}")
    public ResponseEntity<List<Cuenta>> obtenerCuentasUsuarios(@PathVariable String idCuenta) {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerCuentasUsuarios(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idCuenta}/anularCuenta")
    public ResponseEntity<String> anularCuenta(@PathVariable Long idCuenta) {
        try {
            return ResponseEntity.ok(servicioUsuario.anularCuenta(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idCuenta}/activarCuenta")
    public ResponseEntity<String> activarCuenta(@PathVariable Long idCuenta) {
        try {
            return ResponseEntity.ok(servicioUsuario.activarCuenta(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*



    @GetMapping("/obtenerViaje/{idViaje}/{idUsuario}")
    public ResponseEntity<Viaje> obtenerViaje(Long idViaje, Long idUsuario) {
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.obtenerViaje(idViaje, idUsuario));
            }else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/obtenerViajesUsuario/{idUsuario}")
    public ResponseEntity<List<Viaje>> obtenerViajesUsuario(Long idUsuario) {
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.obtenerViajesUsuario(idUsuario));
            }else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/agregarViaje/{Viaje}")
    public ResponseEntity<Viaje> agregarViaje(Viaje v, Long idUsuario) {
        try {
            if(servicioUsuario.existeUsuario(v.getIdUsuario())) {
                //TODO ¿deberia agregar a un usuario su lista de viajes?
                Viaje nuevoViaje = servicioUsuario.agregarViaje(v);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoViaje);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/

    //d. Como administrador quiero consultar el total facturado en un rango de meses de cierto año.*/
    /*@GetMapping("")
    public ResponseEntity<Double> obtenerTotalFacturado(int anioDeseado, int mesDeseadoInicial, int mesDeseadoFinal) {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerTotalFacturado(anioDeseado, mesDeseadoInicial, mesDeseadoFinal));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }*/

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
        /*@GetMapping("/obtenerUsuariosHabilitados/")
    public ResponseEntity<List<DTOUsuario>> obtenerUsuariosHabilitados() {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerUsuariosHabilitados());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/obtenerUsuariosDeshabilitados/")
    public ResponseEntity<List<DTOUsuario>> obtenerUsuariosDeshabilitados() {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerUsuariosDeshabilitados());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/

    /*@PutMapping("/habilitarUsuario/{idUsuario}")
    public ResponseEntity<DTOUsuario> habilitarUsuario(Long idUsuario) {
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.habilitarUsuario(idUsuario));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/deshabilitarUsuario/{idUsuario}")
    public ResponseEntity<DTOUsuario> deshabilitarUsuario(Long idUsuario) {
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.deshabilitarUsuario(idUsuario));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/
}


