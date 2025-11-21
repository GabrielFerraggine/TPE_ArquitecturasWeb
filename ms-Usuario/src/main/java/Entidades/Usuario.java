package Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    @Column
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

    //Usa un enumerado (usuario, mantenimiento, admin)
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column
    private double latitud;

    @Column
    private double longitud;

    //Guarda las id de los monopatines utilizados
    @ElementCollection
    @CollectionTable(
            name = "usuario_monopatines",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "monopatin_id")
    private List<Long> monopatines = new ArrayList<>();

    //Guarda las id de los viajes realizados
    @ElementCollection
    @CollectionTable(
            name = "usuario_viajes",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "viaje_id")
    private List<Long> viajes = new ArrayList<>();
}