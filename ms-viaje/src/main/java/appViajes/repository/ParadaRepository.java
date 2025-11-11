package appViajes.repository;

import appViajes.entity.Parada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, Long> {

    Optional<Parada> findByIdAndActivaTrue(Long id);

    @Query("SELECT p FROM Parada p WHERE " +
            "SQRT(POWER((p.latitud - :lat), 2) + " +
            "POWER((p.longitud - :lon), 2)) * 111000 <= p.radioPermitidoMetros " +
            "AND p.activa = true")
    List<Parada> findParadasCercanas(@Param("lat") Double lat,
                                     @Param("lon") Double lon);

    @Query("SELECT COUNT(p) > 0 FROM Parada p WHERE " +
            "SQRT(POWER((p.latitud - :lat), 2) + " +
            "POWER((p.longitud - :lon), 2)) * 111000 <= p.radioPermitidoMetros " +
            "AND p.activa = true")
    Boolean paradaValida(@Param("lat") Double lat,
                               @Param("lon") Double lon);

    @Query("SELECT p FROM Parada p " +
            "WHERE p.id = :id ")
    Parada obtenerParadaPorId(@Param("id") Long id);

}
