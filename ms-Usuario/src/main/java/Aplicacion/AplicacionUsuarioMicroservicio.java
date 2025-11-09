package Aplicacion;

import Utils.CargarDatos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
public class AplicacionUsuarioMicroservicio {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AplicacionUsuarioMicroservicio.class, args);

        try {
            CargarDatos datosUsuario = context.getBean(CargarDatos.class);
            datosUsuario.cargarDatosCSV();
        } catch (IOException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }

	}

    /*  // Inicia la aplicación y devuelve el contexto
        ConfigurableApplicationContext context = SpringApplication.run(Ejecutable.class, args);

        try {
            // Obtener el bean gestionado por Spring
            CargarDatos cargaDeDatos = context.getBean(CargarDatos.class);

            // Ejecutar la carga de datos inicial
            cargaDeDatos.cargarDatosCSV();
        } catch (IOException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }*/

}
