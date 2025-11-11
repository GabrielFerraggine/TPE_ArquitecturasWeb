package Entidades;

import jakarta.persistence.*;
import lombok.*;
import Modelos.*;

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

    //@ManyToMany(mappedBy = "usuarios")
    //private List<Cuenta> cuentas;

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

    //@OneToMany(mappedBy = "usuario")
    //private List<Monopatin> monopatines;

    //@OneToMany(mappedBy = "usuario")
    //private List<Viaje> viajes;


}

