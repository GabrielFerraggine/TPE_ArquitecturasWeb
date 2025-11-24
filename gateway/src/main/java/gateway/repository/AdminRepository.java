package gateway.repository;

import gateway.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Collection;

@Repository
public interface AdminRepository extends JpaRepository<Rol, String> {
    List<Rol> findByNameIn(Collection<String> names);
}
