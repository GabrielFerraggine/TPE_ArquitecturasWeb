package Repository;

import Entidades.Usuario;
import Modelos.*;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositoryUsuario extends JpaRepository<Usuario, Long>{

    //Obtener todos los Usuarios habilitados (funcion Admin)
    @Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.habilitado = true")
    List<Usuario> obtenerHabilitados();

    //Obtener un usuario mediante su id
    @Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.idUsuario = :id")
    Usuario obtenerUsuario(@Param("id") Long id);

    //Obtener todas los Usuarios deshabilitados (funcion Admin)
    @Query("SELECT u " +
            "FROM Usuario u " +
            "WHERE u.habilitado = false")
    List<Usuario> obtenerDeshabilitados();

    //Obtener todos los usuarios
    @Query("SELECT u " +
            "FROM Usuario u")
    List<Usuario> obtenerUsuarios();

    //Habilitar Usuario (Funcion de admin)
    @Modifying
    @Query("UPDATE Usuario u " +
            "SET u.habilitado = true " +
            "WHERE u.idUsuario = :id")
    void habilitar(@Param("id") Long id);

    //Deshabilitar Usuario (Funcion de admin)
    @Modifying
    @Query("UPDATE Usuario u " +
            "SET u.habilitado = false " +
            "WHERE u.idUsuario = :id")
    void deshabilitar(@Param("id") Long id);
}
