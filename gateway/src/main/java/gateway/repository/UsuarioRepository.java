package gateway.repository;

import gateway.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query(" FROM Usuario u JOIN FETCH u.admins WHERE lower(u.username) =  ?1")
    Optional<Usuario> findOneWithAuthoritiesByUsernameIgnoreCase(String username);
}
