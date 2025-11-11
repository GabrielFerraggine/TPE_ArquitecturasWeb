package appViajes;

import appViajes.Utils.CargarDatos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class MicroservicioViajeApplication {
    public static void main(String[] args) {
            ConfigurableApplicationContext context = SpringApplication.run(MicroservicioViajeApplication.class, args);

            try {
                CargarDatos cargaDeDatos = context.getBean(CargarDatos.class);
                cargaDeDatos.cargarDatosCSV();

            }   catch (Exception e) {
                System.err.println("Error inesperado al cargar los datos: " + e.getMessage());
                e.printStackTrace();
            }

    }
}
