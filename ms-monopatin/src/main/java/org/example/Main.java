package org.example;

import Utils.CargarDatos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.example", "Utils", "repository", "entity", "controller", "service"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        try {
            CargarDatos cargaDeDatos = context.getBean(CargarDatos.class);
            cargaDeDatos.cargarDatosCSV();

        }   catch (Exception e) {
            System.err.println("Error inesperado al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

}