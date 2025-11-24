package Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    @Column(name = "id_usuario")
    private String idUsuario;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuario_cuenta",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "cuenta_id")
    )
    private List<Cuenta> cuentas = new ArrayList<>();

    @Column
    private String nroTelefono;

    @Column
    private String mail;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column
    private double latitud;

    @Column
    private double longitud;

    @ElementCollection
    @CollectionTable(
            name = "usuario_monopatines",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "monopatin_id")
    private List<Long> monopatines = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "usuario_viajes",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "viaje_id")
    private List<Long> viajes = new ArrayList<>();

    @Column(name = "password")
    private String password;

    public void agregarCuenta(Cuenta cuenta) {
        if (this.cuentas == null) {
            this.cuentas = new ArrayList<>();
        }
        if (!this.cuentas.contains(cuenta)) {
            this.cuentas.add(cuenta);
            cuenta.agregarUsuario(this);
        }
    }
}