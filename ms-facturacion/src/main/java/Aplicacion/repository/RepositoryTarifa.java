package Aplicacion.repository;

import Aplicacion.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTarifa extends JpaRepository<Tarifa, Long> {

}
