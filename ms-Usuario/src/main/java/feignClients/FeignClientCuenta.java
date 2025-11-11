package feignClients;

import Modelos.Cuenta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@FeignClient(name="ms-cuenta", url = "http://localhost:8001/api/cuentas")
public interface FeignClientCuenta {
    //Obtener una cuenta
    @GetMapping("/dni/{dni}")
    List<Cuenta> obtenerCuentaUsuario(@PathVariable("dni") String dni);

    //Obtener las cuentas de un usuario
    @GetMapping("/dni/{dni}")
    List<Cuenta> obtenerCuentasUsuario(@PathVariable("dni") String dni);

    //Anular cuenta
    @PutMapping("/{id}/anular")
    String anularCuenta(@PathVariable("id") Long id);

    //Activar cuenta
    @PutMapping("/{id}/activar")
    String activarCuenta(@PathVariable("id") Long id);



    //TODO ¿Donde hacemos el agregar usuario a una cuenta?
}
