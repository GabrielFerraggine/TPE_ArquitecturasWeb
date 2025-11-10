package Aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"Aplicacion", "Utils", "Repository", "Entidades", "Controlador", "Servicio"})
@EnableJpaRepositories(basePackages = "Repository")
@EntityScan(basePackages = "Entidades")  // ← ESTA LÍNEA ES CLAVE
public class AplicacionUsuarioMicroservicio {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionUsuarioMicroservicio.class, args);
    }
}