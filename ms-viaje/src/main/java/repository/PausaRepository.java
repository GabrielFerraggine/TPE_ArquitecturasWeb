package repository;

import entity.Pausa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pausaRepository")
public interface PausaRepository extends JpaRepository<Pausa, Long> {
    List<Pausa> findByViajeId(Long viajeId);
}
