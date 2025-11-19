package feignClients;


import Modelos.Monopatin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-monopatin", url = "http://localhost:8005/monopatin")
public interface FeignClientMonopatin {

    /*g. Como usuario quiero un listado de los monopatines cercanos a mi zona, para poder encontrar
    un monopatín cerca de mi ubicación*/
    @GetMapping("/obtenerMonopatinesCercanos/{latitud}/{longitud}")
    List<Monopatin> obtenerMonopatinesCercanos(
            @PathVariable double latitud,
            @PathVariable double longitud);
}
