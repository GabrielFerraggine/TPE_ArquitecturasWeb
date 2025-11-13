package appViajes.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="ms-monopatin", url="http://localhost:8005/api/monopatin")
public interface MonopatinFeignClient  {

    // /api/monopatines/{id}/cambiarEstado/{estado}
    @PutMapping("/{id}/cambiarEstado/{estado}")
    void actualizarEstadoMonopatin(@PathVariable Long id, @RequestParam String estado);

    @GetMapping("/{id}/LIBRE")
    boolean verificarMonopatinActivo(@PathVariable Long id);

    @GetMapping("/{id}/latitud")
    double obtenerLatitud(@PathVariable Long id);

    @GetMapping("/{id}/longitud")
    double obtenerLongitud(@PathVariable Long id);

    @GetMapping("/{id}/enParada")
    boolean verificarMonopatinEnParada(@PathVariable Long id);

    @PutMapping("/finalizarRecorrido/{idMonopatin}/{kmRecorridos}/{tiempoDeUsoTotal}/{tiempoDePausas}")
    void finalizarRecorrido(@PathVariable("idMonopatin") Long idMonopatin, @PathVariable double kmRecorridos, @PathVariable int tiempoDeUsoTotal, @PathVariable int tiempoDePausas);

}
