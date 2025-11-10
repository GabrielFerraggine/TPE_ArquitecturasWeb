package Aplicacion.Utils;

import Aplicacion.dtos.CuentaResponseDTO;
import Aplicacion.entity.Cuenta;
import Aplicacion.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ToDTOMapper {

    public CuentaResponseDTO toCuentaResponseDTO(Cuenta cuenta) {
        CuentaResponseDTO cuentaResponseDTO = new CuentaResponseDTO();
        cuentaResponseDTO.setNroCuenta(cuenta.getNroCuenta());
        cuentaResponseDTO.setSaldo(cuenta.getSaldo());
        cuentaResponseDTO.setUsuarios(obtenerUsuarios(cuenta));
        cuentaResponseDTO.setFechaAlta(cuenta.getFechaAlta());
        cuentaResponseDTO.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaResponseDTO.setIdCuentaMP(cuenta.getIdCuentaMP());
        cuentaResponseDTO.setActivo(cuenta.isActivo());
        return cuentaResponseDTO;
    }

    private List<String> obtenerUsuarios(Cuenta cuenta) {
        List<Usuario> usuarios = cuenta.getUsuarios();
        List<String> listaUsuarios = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            listaUsuarios.add(usuario.getDni());
        }

        return listaUsuarios;
    }

    public List<CuentaResponseDTO> toCuentaResponseDTO(List<Cuenta> cuentas) {
        List<CuentaResponseDTO> cuentasResponseDTO = new ArrayList<>();
        for (Cuenta cuenta : cuentas) {
            cuentasResponseDTO.add(toCuentaResponseDTO(cuenta));
        }
        return cuentasResponseDTO;
    }
}
