package gateway.config;

import gateway.security.AdminConstant;
import gateway.security.jwt.JwtFilter;
import gateway.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    public SecurityConfig( TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain( final HttpSecurity http ) throws Exception {
        http
                .csrf( AbstractHttpConfigurer::disable );
        http
                .sessionManagement( s -> s.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );
        http
                .securityMatcher("/api/**" )/**/
                .authorizeHttpRequests( authz -> authz
                        //6 de admin (a - f), 2 de usuario (g - h)
                        .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/monopatin/reporte/kmRecorridos**").hasAnyAuthority( AdminConstant._ADMIN)//punto A
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/{dni}/**").hasAuthority( AdminConstant._ADMIN)//Punto B (Anular/Activar cuenta)
                        .requestMatchers(HttpMethod.GET, "/api/viaje/viajesFrecuentes/{cantidadMinima}/{anio}").hasAuthority( AdminConstant._ADMIN)//Punto C
                        .requestMatchers(HttpMethod.GET, "/api/factura/totalFacturado/{anio}/{mesInicio}/{mesFin}").hasAuthority( AdminConstant._ADMIN)//Punto D
                        //TODO .requestMatchers( "/api/usuario/**").hasAuthority( AdminConstant._ADMIN)//PUNTO e
                        .requestMatchers(HttpMethod.POST, "/api/tarifa/ajustarPrecios/{nuevaTarifaBase}/{nuevaTarifaExtra}/{fechaInicio}").hasAuthority( AdminConstant._ADMIN)//Punto F
                        .requestMatchers(HttpMethod.GET, "/api/usuario/obtenerMonopatinesCercanos/{latitud}/{longitud}").hasAuthority( AdminConstant._USUARIO)//Punto G
                        //TODO .requestMatchers("/api/usuario/tipoUsoMonopatines/{idUsuario}").hasAuthority( AdminConstant._USUARIO)//Punto H
                        //.requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic( Customizer.withDefaults() )
                .addFilterBefore( new JwtFilter( this.tokenProvider ), UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

}
