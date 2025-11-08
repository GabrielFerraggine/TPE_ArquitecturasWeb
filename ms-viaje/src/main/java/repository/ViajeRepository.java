package repository;

import entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("viajeRepository")
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    List<Viaje> findByIdUsuario(Long idUsuario);
    List<Viaje> findByIdMonopatin(Long idMonopatin);
    List<Viaje> findByIdCuenta(String idCuenta);
    List<Viaje> findByFechaHoraInicioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Querys
    //
}
