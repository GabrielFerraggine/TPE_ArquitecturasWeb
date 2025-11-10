package feignClients;

import Modelos.Facturacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name="ms-facturacion", url = "http://localhost:8004/api/facturacion")
public interface FeignClientFacturacion {
    /*d. Como administrador quiero consultar el total facturado en un rango de meses de cierto año.*/
    @GetMapping("/totalFacturado/{anio}/{mesInicio}/{mesFin}")
    Double obtenerTotalFacturado(@PathVariable("anio") int anio,
                                 @PathVariable("mesInicio") int mesInicio,
                                 @PathVariable("mesFin") int mesFin);
}
