package appViajes.feignClients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-facturacion", url = "http://localhost:8004/api")
public interface FacturacionFeignClient {


}
