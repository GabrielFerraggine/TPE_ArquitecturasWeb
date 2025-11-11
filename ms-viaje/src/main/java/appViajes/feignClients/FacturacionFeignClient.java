package appViajes.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-facturacion", url = "http://localhost:8004/api")
public interface FacturacionFeignClient {

    //@PostMapping("/facturacion/generar-factura")

}
