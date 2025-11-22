package appViajes.repository;

import appViajes.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository("viajeRepository")
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    List<Viaje> findByIdUsuario(Long idUsuario);
    List<Viaje> findByIdMonopatin(Long idMonopatin);
    List<Viaje> findByIdCuenta(Long idCuenta);
    List<Viaje> findByFechaHoraInicioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Querys
    //
      // reportarViajes
    @Query("SELECT v FROM Viaje v")
    List<Viaje> reportarViajes();

    @Query("SELECT v FROM Viaje v " +
            "WHERE v.estado = 'EN_CURSO' " +
            "AND v.idMonopatin = :idMonopatin")
    Viaje findViajeActivoByMonopatin(@Param("idMonopatin") Long idMonopatin);

    @Query("SELECT v FROM Viaje v " +
            "WHERE v.estado = 'EN_CURSO' " +
            "AND v.idUsuario = :idUsuario")
    Viaje findViajeActivoByUsuario(@Param("idUsuario") Long idUsuario);


    @Query("SELECT new map(v.idMonopatin as idMonopatin, COUNT(v) as totalViajes) " +
            "FROM Viaje v " +
            "WHERE YEAR(v.fechaHoraInicio) = :anio " +
            "AND v.estado = 'FINALIZADO' " +
            "GROUP BY v.idMonopatin " +
            "HAVING COUNT(v) > :cantidadMinima")
    List<Map<String, Object>> findMonopatinesConMasDeXViajesEnAnio(
            @Param("cantidadMinima") Long cantidadMinima,
            @Param("anio") Integer anio);

    @Query(value = "SELECT COALESCE(SUM(TIMESTAMPDIFF(MINUTE, v.fecha_hora_inicio, COALESCE(v.fecha_hora_fin, CURRENT_TIMESTAMP))), 0) " +
            "FROM viaje v " +
            "WHERE v.id_usuario = :idUsuario " +
            "AND v.fecha_hora_inicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO')", nativeQuery = true)
    Integer findTiempoUsoTotalPorUsuarioYPeriodo(@Param("idUsuario") Long idUsuario,
                                               @Param("fechaInicio") LocalDateTime fechaInicio,
                                               @Param("fechaFin") LocalDateTime fechaFin);

    @Query(value = "SELECT COALESCE(SUM(TIMESTAMPDIFF(MINUTE, v.fecha_hora_inicio, IFNULL(v.fecha_hora_fin, CURRENT_TIMESTAMP))), 0) " +
            "FROM viaje v " +
            "WHERE v.id_cuenta IN (SELECT DISTINCT v2.id_cuenta FROM viaje v2 WHERE v2.id_usuario = :idUsuario) " +
            "AND v.fecha_hora_inicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO')", nativeQuery = true)
    Integer findTiempoUsoTotalPorCuentasRelacionadas(@Param("idUsuario") Long idUsuario,
                                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                                   @Param("fechaFin") LocalDateTime fechaFin);

    // Top usuarios por uso (todos los usuarios)
    @Query(value = "SELECT v.id_usuario, " +
            "COUNT(*) as cantidadViajes, " +
            "SUM(TIMESTAMPDIFF(MINUTE, v.fecha_hora_inicio, IFNULL(v.fecha_hora_fin, CURRENT_TIMESTAMP))) as tiempoTotalMinutos " +
            "FROM viaje v " +
            "WHERE v.fecha_hora_inicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO') " +
            "GROUP BY v.id_usuario " +
            "ORDER BY tiempoTotalMinutos DESC", nativeQuery = true)
    List<Object[]> findTopUsuariosPorUso(@Param("fechaInicio") LocalDateTime fechaInicio,
                                       @Param("fechaFin") LocalDateTime fechaFin);

    // Top usuarios premium por uso
    @Query(value = "SELECT v.id_usuario, " +
            "COUNT(*) as cantidadViajes, " +
            "SUM(TIMESTAMPDIFF(MINUTE, v.fecha_hora_inicio, COALESCE(v.fecha_hora_fin, CURRENT_TIMESTAMP))) as tiempoTotalMinutos " +
            "FROM viaje v " +
            "WHERE v.fecha_hora_inicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO') " +
            "AND v.id_cuenta IN :cuentasPremium " +
            "GROUP BY v.id_usuario " +
            "ORDER BY tiempoTotalMinutos DESC", nativeQuery = true)
    List<Object[]> findTopUsuariosPremiumPorUso(@Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin,
                                              @Param("cuentasPremium") List<Long> cuentasPremium);

    // Top usuarios básicos por uso
    @Query(value = "SELECT v.id_usuario, " +
            "COUNT(*) as cantidadViajes, " +
            "SUM(TIMESTAMPDIFF(MINUTE, v.fecha_hora_inicio, COALESCE(v.fecha_hora_fin, CURRENT_TIMESTAMP))) as tiempoTotalMinutos " +
            "FROM viaje v " +
            "WHERE v.fecha_hora_inicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO') " +
            "AND v.id_cuenta NOT IN :cuentasPremium " +
            "GROUP BY v.id_usuario " +
            "ORDER BY tiempoTotalMinutos DESC", nativeQuery = true)
    List<Object[]> findTopUsuariosBasicosPorUso(@Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin,
                                              @Param("cuentasPremium") List<Long> cuentasPremium);
}

