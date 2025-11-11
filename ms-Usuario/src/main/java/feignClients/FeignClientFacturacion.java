package feignClients;

import Modelos.Facturacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@FeignClient(name="ms-facturacion", url = "http://localhost:8004/api/facturacion")
public interface FeignClientFacturacion {
    /*d. Como administrador quiero consultar el total facturado en un rango de meses de cierto año.*/
    @GetMapping("/totalFacturado/{anio}/{mesInicio}/{mesFin}")
    Double obtenerTotalFacturado(@PathVariable("anio") int anio,
                                 @PathVariable("mesInicio") int mesInicio,
                                 @PathVariable("mesFin") int mesFin);


    /*f. Como administrador quiero hacer un ajuste de precios, y que a partir de cierta fecha el sistema habilite los nuevos precios*/
    @PostMapping("/ajustarPrecios/{nuevaTarifaBase}/{nuevaTarifaExtra}/{fechaInicio}")
    ResponseEntity<Void> ajustarPreciosTarifas(@PathVariable BigDecimal nuevaTarifaBase,
                                              @PathVariable BigDecimal nuevaTarifaExtra,
                                              @PathVariable LocalDate fechaInicio);

    }
