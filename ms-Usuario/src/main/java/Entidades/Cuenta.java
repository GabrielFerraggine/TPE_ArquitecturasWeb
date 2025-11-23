package Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cuenta")
@Entity
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroCuenta;

    @ManyToMany(mappedBy = "cuentas", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Usuario> usuarios = new ArrayList<>();

    public Cuenta(Long nroCuenta) {
        this.nroCuenta = nroCuenta;
        this.usuarios = new ArrayList<>();
    }

    public void setIdCuenta(Long id) {
        this.nroCuenta = id;
    }

    public void agregarUsuario(Usuario usuario) {
        if (this.usuarios == null) {
            this.usuarios = new ArrayList<>();
        }
        if (!this.usuarios.contains(usuario)) {
            this.usuarios.add(usuario);
        }
    }
}