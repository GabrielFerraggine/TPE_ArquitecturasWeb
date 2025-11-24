package Repository;

import Entidades.Rol;
import Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuario, String>{

    //Obtener un usuario mediante su id
    @Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.idUsuario = :id")
    Usuario obtenerUsuario(@Param("id") String id);

    //Obtener todos los usuarios
    @Query("SELECT u " +
            "FROM Usuario u")
    List<Usuario> obtenerUsuarios();

    //Obtener todos los usuarios por rol
    @Query("SELECT u.idUsuario " +
            "FROM Usuario u " +
            "WHERE u.rol = :rol")
    List<String> obtenerUsuariosPorRol(@Param("rol") Rol rol);
}
