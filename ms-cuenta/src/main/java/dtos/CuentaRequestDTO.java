package dtos;

import entity.TipoCuenta;
import lombok.Data;

import java.util.List;

@Data
public class CuentaRequestDTO {
    private List<String> usuarios;
    private TipoCuenta tipoCuenta;
    private String idCuentaMP;
}
