package Repository;

import Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuario, Long>{

    //Obtener todos los Usuarios habilitados
    /*@Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.habilitado = true")
    List<Usuario> obtenerHabilitados();*/

    //Obtener un usuario mediante su id
    @Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.idUsuario = :id")
    Usuario obtenerUsuario(@Param("id") Long id);

    //Obtener todas los Usuarios deshabilitados
    /*@Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.habilitado = false")
    List<Usuario> obtenerDeshabilitados();*/

    //Obtener todos los usuarios
    @Query("SELECT u " +
            "FROM Usuario u")
    List<Usuario> obtenerUsuarios();

    //Habilitar Usuario
    /*@Modifying
    @Query("UPDATE Usuario u " +
            "SET u.habilitado = true " +
            "WHERE u.idUsuario = :id")
    void habilitar(@Param("id") Long id);*/

    //Deshabilitar Usuario
    /*@Modifying
    @Query("UPDATE Usuario u " +
            "SET u.habilitado = false " +
            "WHERE u.idUsuario = :id")
    void deshabilitar(@Param("id") Long id);*/
}
