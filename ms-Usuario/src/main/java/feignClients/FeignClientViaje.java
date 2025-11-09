package feignClients;

import Modelos.Viaje;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-viaje", url = "http://localhost:8003/viaje")
public interface FeignClientViaje {
    //Obtener un viaje
    @GetMapping("/obtenerViaje/{idViaje}/{idUsuario}")
    Viaje obtenerViaje(@PathVariable("idViaje") Long idViaje,
                       @PathVariable("idUsuario") Long idUsuario);

    //Obtener todos los viajes de un usuario
    @GetMapping("/obtenerViajes/{idUsuario}")
    List<Viaje> obtenerViajesUsuario(@PathVariable("idUsuario") Long idUsuario);

    //Agregar un nuevo viaje
    @PostMapping
    Viaje save(@RequestBody Viaje viaje);

}
