package org.example.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-cuenta", url="http://localhost:8001/api")
public interface CuentaFeignClient {

    @GetMapping("/cuentas/{id}/premium")
    boolean verificarCuentaPremium(@PathVariable Long id);
}
