package Entidades;

import jakarta.persistence.*;
import lombok.*;

@Data
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    private Long idUsuario;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @ManyToMany(mappedBy = "usuario")
    private List<Cuenta> cuentas;

    @Column
    private String nroTelefono;

    @Column
    private String mail;

    //Usa un enumerado (usuario, mantenimiento, admin)
    @Column
    private Enum rol;

    @Column
    private int latitud;

    @Column
    private int longitud;

    @OneToMany(mappedBy = "usuario")
    private List<Monopatin> monopatines;

    @OneToMany(mappedBy = "usuario")
    private List<Viaje> viajes;

}

