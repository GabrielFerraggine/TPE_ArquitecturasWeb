package Aplicacion.Utils;

import Aplicacion.dtos.CuentaRequestDTO;
import Aplicacion.entity.Cuenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ToEntityMapper {

    public Cuenta toCuenta(CuentaRequestDTO cuentaRequestDTO) {
        Cuenta cuenta = new Cuenta();

        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setFechaAlta(new Date());
        cuenta.setTipoCuenta(cuentaRequestDTO.getTipoCuenta());
        cuenta.setIdCuentaMP(cuentaRequestDTO.getIdCuentaMP());
        cuenta.setActivo(true);
        return cuenta;
    }
}
