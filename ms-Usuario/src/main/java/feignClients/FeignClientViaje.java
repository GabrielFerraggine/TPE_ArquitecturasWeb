package feignClients;

import DTO.DTOTiempoDeViaje;
import Modelos.Viaje;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-viaje", url = "http://localhost:8003/viaje")
public interface FeignClientViaje {

    //c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año
    @GetMapping("/monopatinesMasUsados/{anio}/{cantidadMinimaViajes}")
    List<Long> obtenerMonopatinesMasUsados(@PathVariable("anio") int anio,
                                           @PathVariable("cantidadMinimaViajes") Long cantidadMinimaViajes);


}
