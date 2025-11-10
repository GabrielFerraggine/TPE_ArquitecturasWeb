package Aplicacion.dtos;

import Aplicacion.entity.TipoCuenta;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CuentaRequestDTO {
    @Size(min = 1, message = "Debe haber al menos un usuario")
    private List<String> usuarios;
    private TipoCuenta tipoCuenta;
    private String idCuentaMP;
}
