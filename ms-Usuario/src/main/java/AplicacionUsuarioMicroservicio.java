import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AplicacionUsuarioMicroservicio {

	public static void main(String[] args) {
		SpringApplication.run(AplicacionUsuarioMicroservicio.class, args);
	}

}
