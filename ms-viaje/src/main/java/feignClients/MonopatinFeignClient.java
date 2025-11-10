package feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="ms-monopatin", url="${ms-monopatin.url}")
public interface MonopatinFeignClient  {

    @PutMapping("/monopatines/{id}/estado")
    void actualizarEstadoMonopatin(@PathVariable Long id, @RequestParam String estado);

    @GetMapping("/monopatines/{id}/disponible")
    boolean verificarMonopatinActivo(@PathVariable Long id);

    @GetMapping("/monopatines/{id}/latitud")
    double obtenerLatitud(@PathVariable Long id);

    @GetMapping("/monopatines/{id}/longitud")
    double obtenerLongitud(@PathVariable Long id);

    @GetMapping("/monopatines/{id}/enParada")
    boolean verificarMonopatinEnParada(@PathVariable Long id);



}
