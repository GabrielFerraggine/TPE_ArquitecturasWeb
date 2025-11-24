package Aplicacion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroCuenta;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cuenta_usuario",
            joinColumns = @JoinColumn(name = "cuenta_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios = new ArrayList<>(); //dnis

    @Column
    private BigDecimal saldo; // cargado por MP

    @Column
    private Date fechaAlta;

    @Column
    private TipoCuenta tipoCuenta; // premium o basica

    @Column
    private String idCuentaMP;

    @Column
    private boolean activo; //estado de cuenta: anulada o no

    public boolean isPremium() {
        return tipoCuenta == TipoCuenta.PREMIUM;
    }

}
