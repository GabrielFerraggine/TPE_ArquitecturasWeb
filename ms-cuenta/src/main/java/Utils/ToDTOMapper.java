package Utils;

import dtos.CuentaResponseDTO;
import entity.Cuenta;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ToDTOMapper {

    public CuentaResponseDTO toCuentaResponseDTO(Cuenta cuenta) {
        CuentaResponseDTO cuentaResponseDTO = new CuentaResponseDTO();
        cuentaResponseDTO.setNroCuenta(cuenta.getNroCuenta());
        cuentaResponseDTO.setSaldo(cuenta.getSaldo());
        cuentaResponseDTO.setFechaAlta(cuenta.getFechaAlta());
        cuentaResponseDTO.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaResponseDTO.setIdCuentaMP(cuenta.getIdCuentaMP());
        cuentaResponseDTO.setActivo(cuenta.isActivo());
        return cuentaResponseDTO;
    }

    public List<CuentaResponseDTO> toCuentaResponseDTO(List<Cuenta> cuentas) {
        List<CuentaResponseDTO> cuentasResponseDTO = new ArrayList<>();
        for (Cuenta cuenta : cuentas) {
            cuentasResponseDTO.add(toCuentaResponseDTO(cuenta));
        }
        return cuentasResponseDTO;
    }
}
