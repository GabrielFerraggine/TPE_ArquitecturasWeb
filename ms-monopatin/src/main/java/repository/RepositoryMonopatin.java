package repository;

import java.util.Optional;
import entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("RepositoryMonopatin")
public interface RepositoryMonopatin extends JpaRepository<Monopatin, Long> {

    @Query("SELECT m FROM Monopatin m WHERE m.idMonopatin = :id")
    Monopatin buscarPorId(Long id);

}