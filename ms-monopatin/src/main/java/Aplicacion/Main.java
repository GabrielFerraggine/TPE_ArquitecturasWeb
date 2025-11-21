package Aplicacion;

import Aplicacion.Utils.CargarDatos;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Microservicio de monopatín",
                version = "1.0.0",
                description = "Módulo de gestión de monopatines."
        )
)
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