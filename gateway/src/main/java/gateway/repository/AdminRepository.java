package gateway.repository;

import gateway.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Collection;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    List<Admin> findByNameIn(Collection<String> names);
}
