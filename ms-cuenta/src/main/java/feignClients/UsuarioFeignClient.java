package feignClients;

import entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-usuario", url = "http://localhost:8001/usuario")
public interface UsuarioFeignClient {

    @GetMapping("/{dniUsuario}")
    Usuario buscarUsuarioPorDni(@PathVariable("dniUsuario") String dni);

    @GetMapping
    List<Usuario> buscarTodosLosUsuarios();
}
