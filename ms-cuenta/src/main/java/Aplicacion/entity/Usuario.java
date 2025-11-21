package Aplicacion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

@ManyToMany(mappedBy = "usuarios", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cuenta> cuentas = new ArrayList<>();
}

