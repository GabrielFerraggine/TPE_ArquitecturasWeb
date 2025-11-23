package feignClients;

import DTO.DTOTiempoDeViaje;
import Modelos.Viaje;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-viaje", url = "http://localhost:8003/viaje")
public interface FeignClientViaje {

    //c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año
    @GetMapping("/monopatinesMasUsados/{anio}/{cantidadMinimaViajes}")
    List<Long> obtenerMonopatinesMasUsados(@PathVariable("anio") int anio,
                                           @PathVariable("cantidadMinimaViajes") Long cantidadMinimaViajes);

    /*h. Como usuario quiero saber cuánto he usado los monopatines en un período, y opcionalmente
    si otros usuarios relacionados a mi cuenta los han usado.*/
    @GetMapping("/admin/topUsuarios{fechaInicio}/{fechaFin}/{tipoUsuario}")
    List<Map<String, Object>> obtenerTopUsuarios(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
                                                 @RequestParam(required = false, defaultValue = "TODOS") String tipoUsuario);
}
