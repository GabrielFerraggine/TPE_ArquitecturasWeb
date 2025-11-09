package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroCuenta;
    private List<String> usuarios; //dnis
    private BigDecimal saldo; // cargado por MP
    private Date fechaAlta;
    private TipoCuenta tipoCuenta; // premium o basica
    private String idCuentaMP;
    private boolean activo; //estado de cuenta: anulada o no

}
