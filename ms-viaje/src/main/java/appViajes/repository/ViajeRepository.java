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


}

