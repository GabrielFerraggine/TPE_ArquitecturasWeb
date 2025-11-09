package feignClients;

import Modelos.Cuenta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name="ms-cuenta", url = "http://localhost:8001/cuenta")
public interface FeignClientCuenta {
    //Obtener una cuenta
    @GetMapping("/obtenerCuenta/{idCuenta}")
    Cuenta obtenerCuentaUsuario(@PathVariable("idCuenta") Long idCuenta);

    //Obtener las cuentas de un usuario
    @GetMapping("/obtenerCuentas/{idUsuario}")
    List<Cuenta> obtenerCuentasUsuario(@PathVariable("idUsuario") Long idUsuario);

    //TODO ¿Donde hacemos el agregar usuario a una cuenta?
}
