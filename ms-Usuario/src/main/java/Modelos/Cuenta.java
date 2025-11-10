package Modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
public class Cuenta {
    private Long nroCuenta;
    private List<String> usuarios;
    private BigDecimal saldo;
    private Date fechaAlta;
    private TipoCuenta tipoCuenta;
    private String idCuentaMP;
    private boolean activo;

    public enum TipoCuenta {
        BASICA,
        PREMIUM
    }

}