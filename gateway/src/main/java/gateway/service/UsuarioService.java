package gateway.service;

import gateway.DTOs.UsuarioDTO;
import gateway.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gateway.repository.AdminRepository;
import gateway.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public long saveUser(UsuarioDTO request){
        final var usuario = new Usuario(request.getUsername() );
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        final var roles = adminRepository.findByNameIn(request.getAdmins());
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron roles válidos");
        }
        usuario.setAdmins( roles );
        final var result = this.usuarioRepository.save( usuario );
        return result.getId();
    }

}
