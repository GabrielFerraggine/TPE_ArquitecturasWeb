package Controlador;


import DTO.DTOUsuario;
import Entidades.Usuario;
import Modelos.*;
import Servicio.ServicioUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class ControllerUsuario {

    private final ServicioUsuario servicioUsuario;

    public ControllerUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    /*==============================Llamadas de los FeignClient==============================*/
    /*g. Como usuario quiero un listado de los monopatines cercanos a mi zona,
    para poder encontrar un monopatín cerca de mi ubicación*/
    @GetMapping("/obtenerMonopatinesCercanos/{latitud}/{longitud}")
    public ResponseEntity<List<Monopatin>> obtenerMonopatinesCercanos(@PathVariable int latitud, @PathVariable int longitud) {
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

    @GetMapping("/obtenerCuentaUsuario/{idCuenta}")
    public ResponseEntity<Cuenta> obtenerCuentaUsuario(Long idCuenta) throws Exception{
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerCuentaUsuario(idCuenta));
        } catch (Exception e) {
            return  ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/obtenerCuentasUsuarios/{idCuenta}")
    public ResponseEntity<List<Cuenta>> obtenerCuentasUsuarios(Long idCuenta) throws Exception {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerCuentasUsuarios(idCuenta));
        } catch (Exception e) {
            throw new Exception("No se pudo obtener las cuentas del usuario: " + e.getMessage());
        }
    }

    @GetMapping("/obtenerViaje/{idViaje}/{idUsuario}")
    public ResponseEntity<Viaje> obtenerViaje(Long idViaje, Long idUsuario) throws Exception{
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerViaje(idViaje, idUsuario));
        } catch (Exception e) {
            throw new Exception("no se pudo obtener el viaje: " + idViaje + " del usuario: " + idUsuario + e.getMessage());
        }
    }

    @GetMapping("/obtenerViajesUsuario/{idUsuario}")
    public ResponseEntity<List<Viaje>> obtenerViajesUsuario(Long idUsuario) throws Exception {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerViajesUsuario(idUsuario));
        } catch (Exception e) {
            throw new Exception("No se pudo obtener los viajes del usuario: " + idUsuario + " " + e.getMessage());
        }
    }

    @PostMapping("/agregarViaje/{Viaje}")
    public ResponseEntity<Viaje> agregarViaje(Viaje v, Long idUsuario) throws Exception {
        try {
            if(servicioUsuario.existeUsuario(v.getIdUsuario())) {
                //TODO deberia agregar a un usuario su lista de viajes?
                return ResponseEntity.ok(servicioUsuario.agregarViaje(v));
            } else {
                throw new Exception("No existe el usuario al cual se le quiere agregar el viaje");
            }
        } catch (Exception e) {
            throw new Exception("No se pudo agregar el viaje: " + e.getMessage());
        }
    }

    /*=============================Llamadas de usuario=====================================*/
    @GetMapping("/obtenerUsuario/{idUsuario}")
    public ResponseEntity<DTOUsuario> obtenerUsuario(Long idUsuario) throws Exception {
        try {
            if (servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.obtenerUsuario(idUsuario));
            }
            else {
                throw new Exception("El usuario no existe");
            }
        } catch (Exception e) {
            throw new Exception("No se pudo obtener el usuario con id: " + idUsuario + " " + e.getMessage());
        }
    }

    @GetMapping("/obtenerUsuarios")
    public ResponseEntity<List<DTOUsuario>> obtenerUsuarios() throws Exception {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerUsuarios());
        } catch(Exception e) {
            throw new Exception("No se pudieron obtener todos los usuarios " + e.getMessage());
        }
    }

    @GetMapping("/obtenerUsuariosHabilitados/")
    public ResponseEntity<List<DTOUsuario>> obtenerUsuariosHabilitados() throws Exception {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerUsuariosHabilitados());
        } catch (Exception e) {
            throw new Exception("No se pudo obtener los usuarios habilitados: " + e.getMessage());
        }
    }

    @GetMapping("/obtenerUsuariosDeshabilitados/")
    public ResponseEntity<List<DTOUsuario>> obtenerUsuariosDeshabilitados() throws Exception {
        try {
            return ResponseEntity.ok(servicioUsuario.obtenerUsuariosDeshabilitados());
        } catch (Exception e) {
            throw new Exception("no se pudieron obtener los usuarios deshabilitados: " + e.getMessage());
        }
    }

    @PostMapping("/agregarUsuario/{usuario}")
    public ResponseEntity<DTOUsuario> agregarUsuario(Usuario u) throws Exception {
        try {
            if(!servicioUsuario.existeUsuario(u.getIdUsuario())) {
                return ResponseEntity.ok(servicioUsuario.agregarUsuario(u));
            } else {
                throw new Exception("Ya existe el usuario que se desea agregar");
            }
        } catch (Exception e) {
            throw new Exception("No se pudo agregar al usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminarUsuario/{idUsuario}")
    public ResponseEntity<Boolean> eliminarUsuario(Long idUsuario) throws Exception {
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.eliminarUsuario(idUsuario));
            } else {
                throw new Exception("No existe el usuario con la id: " + idUsuario);
            }
        } catch (Exception e) {
            throw new Exception("No se pudo eliminar el usuario con la id: " + idUsuario + " " + e.getMessage());
        }
    }

    @PutMapping("/actualizarUsuario/{idUsuario}/{usuario}")
    public ResponseEntity<DTOUsuario> actualizarUsuario(Long idUsuario, Usuario u) throws Exception {
        try {
            if(servicioUsuario.existeUsuario(idUsuario)) {
                return ResponseEntity.ok(servicioUsuario.actualizarUsuario(idUsuario, u));
            } else {
                throw new Exception("No existe el usuario que se desea actualizar con la id: " + idUsuario);
            }
        } catch (Exception e) {
            throw new Exception("No se pudo actualizar el usuario con la id: " + idUsuario + " " + e.getMessage());
        }
    }


    //TODO endpoints para habilitar y deshabilitar usuario
}
