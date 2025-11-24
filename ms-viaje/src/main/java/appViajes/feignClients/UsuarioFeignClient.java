package appViajes.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name="ms-usuario", url="http://localhost:8002/api/usuario")
public interface UsuarioFeignClient {

    @GetMapping("/{id}/activo")
    boolean verificarUsuarioActivo(@PathVariable Long id);

    @GetMapping("/obtenerUsuarios")
    List<Long> obtenerUsuarios();

    @GetMapping("/obtenerUsuariosPorRol/{rol}")
    List<String> obtenerUsuariosPorRol(@PathVariable String rol);

}
