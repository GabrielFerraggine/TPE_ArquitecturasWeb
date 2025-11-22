package appViajes.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name="ms-usuario", url="http://localhost:8002/api/usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/{id}/activo")
    boolean verificarUsuarioActivo(@PathVariable Long id);

    @GetMapping("/premiumLista")
    List<Long> obtenerCuentasPremium();


}
