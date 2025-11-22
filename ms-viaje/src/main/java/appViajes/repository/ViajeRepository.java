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

    @Query("SELECT COALESCE(SUM(FUNCTION('TIMESTAMPDIFF', MINUTE, v.fechaHoraInicio, COALESCE(v.fechaHoraFin, CURRENT_TIMESTAMP))), 0) " +
            "FROM Viaje v " +
            "WHERE v.idUsuario = :idUsuario " +
            "AND v.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO')")
    Integer findTiempoUsoTotalPorUsuarioYPeriodo(@Param("idUsuario") Long idUsuario,
                                                 @Param("fechaInicio") LocalDateTime fechaInicio,
                                                 @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COALESCE(SUM(FUNCTION('TIMESTAMPDIFF', MINUTE, v.fechaHoraInicio, COALESCE(v.fechaHoraFin, CURRENT_TIMESTAMP))), 0) " +
            "FROM Viaje v " +
            "WHERE v.idCuenta IN (SELECT DISTINCT v2.idCuenta FROM Viaje v2 WHERE v2.idUsuario = :idUsuario) " +
            "AND v.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO')")
    Integer findTiempoUsoTotalPorCuentasRelacionadas(@Param("idUsuario") Long idUsuario,
                                                     @Param("fechaInicio") LocalDateTime fechaInicio,
                                                     @Param("fechaFin") LocalDateTime fechaFin);

    // Top usuarios por uso (todos los usuarios)
    @Query("SELECT v.idUsuario, " +
            "COUNT(v) as cantidadViajes, " +
            "SUM(FUNCTION('TIMESTAMPDIFF', MINUTE, v.fechaHoraInicio, COALESCE(v.fechaHoraFin, CURRENT_TIMESTAMP))) as tiempoTotalMinutos " +
            "FROM Viaje v " +
            "WHERE v.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO') " +
            "GROUP BY v.idUsuario " +
            "ORDER BY tiempoTotalMinutos DESC")
    List<Object[]> findTopUsuariosPorUso(@Param("fechaInicio") LocalDateTime fechaInicio,
                                         @Param("fechaFin") LocalDateTime fechaFin);

    // Top usuarios premium por uso
    @Query("SELECT v.idUsuario, " +
            "COUNT(v) as cantidadViajes, " +
            "SUM(FUNCTION('TIMESTAMPDIFF', MINUTE, v.fechaHoraInicio, COALESCE(v.fechaHoraFin, CURRENT_TIMESTAMP))) as tiempoTotalMinutos " +
            "FROM Viaje v " +
            "WHERE v.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO') " +
            "AND v.idCuenta IN :cuentasPremium " +
            "GROUP BY v.idUsuario " +
            "ORDER BY tiempoTotalMinutos DESC")
    List<Object[]> findTopUsuariosPremiumPorUso(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                @Param("fechaFin") LocalDateTime fechaFin,
                                                @Param("cuentasPremium") List<Long> cuentasPremium);

    // Top usuarios básicos por uso
    @Query("SELECT v.idUsuario, " +
            "COUNT(v) as cantidadViajes, " +
            "SUM(FUNCTION('TIMESTAMPDIFF', MINUTE, v.fechaHoraInicio, COALESCE(v.fechaHoraFin, CURRENT_TIMESTAMP))) as tiempoTotalMinutos " +
            "FROM Viaje v " +
            "WHERE v.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estado IN ('FINALIZADO', 'EN_CURSO') " +
            "AND v.idCuenta NOT IN :cuentasPremium " +
            "GROUP BY v.idUsuario " +
            "ORDER BY tiempoTotalMinutos DESC")
    List<Object[]> findTopUsuariosBasicosPorUso(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                @Param("fechaFin") LocalDateTime fechaFin,
                                                @Param("cuentasPremium") List<Long> cuentasPremium);
}

