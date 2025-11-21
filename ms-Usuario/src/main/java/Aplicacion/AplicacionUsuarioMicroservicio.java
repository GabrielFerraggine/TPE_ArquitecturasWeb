package Aplicacion;

import feignClients.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"Aplicacion", "Utils", "Repository", "Entidades", "Controlador", "Servicio"})
@EnableJpaRepositories(basePackages = "Repository")
@EntityScan(basePackages = "Entidades")
@EnableFeignClients(basePackageClasses = {
        FeignClientViaje.class,
        FeignClientFacturacion.class,
        FeignClientCuenta.class,
        FeignClientMonopatin.class
})
@OpenAPIDefinition(
        info = @Info(
                title = "Microservicio de Usuario",
                version = "1.0.0",
                description = "Módulo de gestión de usuarios."
        )
)
public class AplicacionUsuarioMicroservicio {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionUsuarioMicroservicio.class, args);
    }
}