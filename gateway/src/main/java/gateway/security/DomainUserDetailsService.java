package gateway.security;

import gateway.entity.Admin;
import gateway.entity.Usuario;
import gateway.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UsuarioRepository userRepository;

    public DomainUserDetailsService( UsuarioRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username ) {
        log.debug("Authenticating {}", username);

        return userRepository
                .findOneWithAuthoritiesByUsernameIgnoreCase( username.toLowerCase() )
                .map( this::createSpringSecurityUser )
                .orElseThrow( () -> new UsernameNotFoundException( "El usuario " + username + " no existe" ) );
    }

    private UserDetails createSpringSecurityUser( Usuario user ) {
        List<GrantedAuthority> grantedAuthorities = user
                .getAdmins()
                .stream()
                .map( Admin::getName )
                .map( SimpleGrantedAuthority::new )
                .collect( Collectors.toList() );

        return new org.springframework.security.core.userdetails.User( user.getUsername(), user.getPassword(), grantedAuthorities );
    }

}
