package dtos;

import entity.TipoCuenta;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CuentaResponseDTO {
    private Long nroCuenta;
    private List<String> usuarios;
    private BigDecimal saldo;
    private Date fechaAlta;
    private TipoCuenta tipoCuenta;
    private String idCuentaMP;
    private boolean activo;
}
