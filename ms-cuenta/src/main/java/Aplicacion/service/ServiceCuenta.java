package Aplicacion.service;

import Aplicacion.Utils.ToDTOMapper;
import Aplicacion.Utils.ToEntityMapper;
import Aplicacion.dtos.CuentaRequestDTO;
import Aplicacion.dtos.CuentaResponseDTO;
import Aplicacion.dtos.SaldoRequestDTO;
import Aplicacion.dtos.SaldoResponseDTO;
import Aplicacion.entity.Cuenta;
import Aplicacion.entity.Usuario;
import Aplicacion.repository.RepositoryUsuario;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Aplicacion.repository.RepositoryCuenta;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServiceCuenta {

    @Autowired
    private RepositoryCuenta repoCuenta;

    @Autowired
    private RepositoryUsuario repoUsuario;

    @Autowired
    private ToDTOMapper toDTOMapper;

    @Autowired
    private ToEntityMapper toEntityMapper;


    public CuentaResponseDTO buscarCuentaPorId(Long numeroCuenta) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return toDTOMapper.toCuentaResponseDTO(cuenta);
    }

    public CuentaResponseDTO crearCuenta(CuentaRequestDTO cuentaDTO){
        Cuenta cuenta = toEntityMapper.toCuenta(cuentaDTO);
        List<Usuario> usuarios = new ArrayList<>();
        for (String dni : cuentaDTO.getUsuarios()) {
            Usuario usuario = repoUsuario.findByDni(dni)
                .orElseGet(() -> {
                    Usuario newUser = new Usuario();
                    newUser.setDni(dni);
                    return repoUsuario.save(newUser);
                });
            usuarios.add(usuario);
        }
        cuenta.setUsuarios(usuarios);

        Cuenta cuentaCreada = repoCuenta.save(cuenta);
        return toDTOMapper.toCuentaResponseDTO(cuentaCreada);
    }

    public List<CuentaResponseDTO> buscarTodasLasCuentas() {
        return toDTOMapper.toCuentaResponseDTO(repoCuenta.findAll());
    }

    public List<CuentaResponseDTO> buscarCuentasPorDNI(String dni) {
        return toDTOMapper.toCuentaResponseDTO(repoCuenta.findByDni(dni));
    }

    public void anularCuenta(Long numeroCuenta) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setActivo(false);
        repoCuenta.save(cuenta);
    }

    public void activarCuenta(Long numeroCuenta) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setActivo(true);
        repoCuenta.save(cuenta);
    }

    public void depositarSaldo(Long numeroCuenta, SaldoRequestDTO saldo) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setSaldo(cuenta.getSaldo().add(saldo.getMonto()));
        repoCuenta.save(cuenta);
    }

    public void extraerSaldo(Long numeroCuenta, SaldoRequestDTO saldo) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setSaldo(cuenta.getSaldo().subtract(saldo.getMonto()));
        repoCuenta.save(cuenta);
    }

    public SaldoResponseDTO verSaldo(Long numeroCuenta) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return new SaldoResponseDTO(numeroCuenta, cuenta.getSaldo());
    }

    public boolean verificarCuentaPremium(@NotNull Long id) {
        Cuenta cuenta = repoCuenta.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return cuenta.isPremium();
    }
}
