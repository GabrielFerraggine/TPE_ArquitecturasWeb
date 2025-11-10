package feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-usuario", url="${ms-usuario.url}")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/{id}/activo")
    boolean verificarUsuarioActivo(@PathVariable Long id);

    @GetMapping("/cuentas/{id}/activa")
    boolean verificarCuentaActiva(@PathVariable Long id);

}
