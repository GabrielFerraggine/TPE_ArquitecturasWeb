package edu.tudai.microserviciofacturacion;

import edu.tudai.microserviciofacturacion.utils.CargarDatos;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(
                title = "Microservicio de facturación",
                version = "1.0.0",
                description = "Módulo de gestión de facturas."
        )
)
public class MicroservicioFacturacionApplication {

    public static void main(String[] args) {
        //inicia la aplicación y guarda el contexto
        ConfigurableApplicationContext context = SpringApplication.run(MicroservicioFacturacionApplication.class, args);

        //llama al bean CargarDatos para cargar
        try {
            CargarDatos cargador = context.getBean(CargarDatos.class);
            cargador.cargarDatosCSV();
            System.out.println("Datos de facturación cargados correctamente.");
        } catch (IOException e) {
            System.err.println("Error al cargar los datos de facturación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}