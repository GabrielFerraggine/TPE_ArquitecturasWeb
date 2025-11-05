package Repository;

import Entidades.Usuario;
import Modelos.*;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositoryUsuario extends JpaRepository<Usuario, Long>{

    /*g. Como usuario quiero un listado de los monopatines cercanos a mi zona, para poder encontrar
    un monopatín cerca de mi ubicación*/
    //Asumo que la trae toda la lista de monopatines y el usuario luego vera los 5 mas cercanos
    /*@Query ("SELECT m " +
            "FROM Monopatin m " +
            "WHERE m.estado = 'libre'")
    List<Monopatin> monopatinesDisponibles;*/
    //Se consume desde monopatin


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
    @Transactional
    @Query("UPDATE Usuario u " +
            "SET u.habilitado = true " +
            "WHERE u.idUsuario = :id")
    void habilitar(@Param("id") Long id);

    //Deshabilitar Usuario (Funcion de admin)
    @Transactional
    @Query("UPDATE Usuario u " +
            "SET u.habilitado = false " +
            "WHERE u.idUsuario = :id")
    void deshabilitar(@Param("id") Long id);

    //Obtener Cuenta
    /*@Query("SELECT u.idCuenta " +
            "FROM Usuario u " +
            "WHERE u.idUsuario = :id")
    List<Long> getIdCuenta(@Param("id") Long id);*/
    //obtenido desde el servicio cuenta, en realidad deberia ser una lista

    //Obtener Monopatin (Funcion de usuario)
    /*@Query("SELECT u.idMonopatinActual " +
            "FROM Usuario u " +
            "WHERE u.id = :id")
    Monopatin getIdMonopatin(@Param("id") Long id);*/
    //obtenido desde monopatin

    //Obtener todos los viajes de un usuario
    /*@Query ("SELECT v " +
            "FROM Viaje v " +
            "WHERE v.idUsuario = :idUsuario")
    List<Viajes> viajesRealizados(@Param("idUsuario") Long idUsuario);*/
    //obtenido desde viaje

    //Obtener todas las cuentas vinculadas a un usuario
    /*@Query ("SELECT c " +
            "FROM Cuenta c " +
            "WHERE c.idUsuario = :idUsuario")
    List<Cuenta> cuentasUsuario(@Param("idUsuario") Long idUsuario);*/
    //obtenido desde cuenta
}
