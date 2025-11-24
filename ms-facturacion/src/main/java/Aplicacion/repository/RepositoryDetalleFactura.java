package Aplicacion.repository;

import Aplicacion.entity.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryDetalleFactura extends JpaRepository<DetalleFactura,Long> {}
