package repository;

import java.util.Optional;
import entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("RepositoryMonopatin")
public interface RepositoryMonopatin extends JpaRepository<Monopatin, Integer> {

    @Query("SELECT m FROM Monopatin m WHERE m.idMonopatin = :id")
    Monopatin buscarPorId(int id);

    @Modifying
    @Query("UPDATE Monopatin m SET m.estado = :estado WHERE m.idMonopatin = :idMonopatin")
    int setEstado(int idMonopatin, String estado);


}