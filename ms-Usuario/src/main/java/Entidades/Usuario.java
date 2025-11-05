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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @ManyToMany(mappedBy = "usuarios")
    private List<Cuenta> cuentas;

    @Column
    private String nroTelefono;

    @Column
    private boolean habilitado;

    @Column
    private String mail;

    //Usa un enumerado (usuario, mantenimiento, admin)
    @Enumerated(EnumType.STRING)
    private roles rol;

    @Column
    private int latitud;

    @Column
    private int longitud;

    @OneToMany(mappedBy = "usuario")
    private List<Monopatin> monopatines;

    @OneToMany(mappedBy = "usuario")
    private List<Viaje> viajes;

    public enum roles {
        ADMINISTRADOR,
        USUARIO,
        TECNICO_MANTENIMIENTO
    }
}

