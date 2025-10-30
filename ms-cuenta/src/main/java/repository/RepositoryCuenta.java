package repository;

import entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("RepositoryCuenta")
public interface RepositoryCuenta extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.nroCuenta = :id")
    Optional<Cuenta> findById(Long id);
    // el optional te da la posibilidad de que no encuentre y te devuelve un empty. Despues hay que tocar el get


}
