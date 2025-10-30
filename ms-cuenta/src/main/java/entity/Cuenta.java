package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroCuenta;
    //TODO consultar por la lista de usuarios.
    //private List<String> usuarios; //dnis
    private double saldo; // cargado por MP
    private boolean activo; //estado de cuenta
    private Date fechaAlta;
    //TODO consultar si hay tipo de cuenta
    //private String tipoCuenta (si hubiese basica o premium)

}
