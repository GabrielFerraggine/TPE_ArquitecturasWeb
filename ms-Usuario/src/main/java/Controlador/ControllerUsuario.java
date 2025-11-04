package Controlador;


import Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class ControllerUsuario {

    @Autowired
    private RepositoryUsuario repoUsuario;


    //Todas las comunicaciones se tienen que hacer mediante el modelo entre los microservicios

}
