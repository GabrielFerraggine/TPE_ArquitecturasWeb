package service;

import Utils.ToDTOMapper;
import dtos.CuentaRequestDTO;
import dtos.CuentaResponseDTO;
import dtos.SaldoRequestDTO;
import entity.Cuenta;
import feignClients.UsuarioFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.RepositoryCuenta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServiceCuenta {

    @Autowired
    private RepositoryCuenta repoCuenta;

    @Autowired
    private ToDTOMapper mapper;

    /*TODO lo necesito?
    @Autowired
    private UsuarioFeignClient usuarioFeignClient;*/

    public CuentaResponseDTO buscarCuentaPorId(Long numeroCuenta) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElse(null);
        if (cuenta == null) {
            throw new RuntimeException("Cuenta no encontrada");
        }
        return mapper.toCuentaResponseDTO(cuenta);
    }

    //TODO modificar para usar cuentaRequest
    public CuentaResponseDTO crearCuenta(Cuenta cuenta){
        Cuenta cuentaGuardada = repoCuenta.save(cuenta);
        return mapper.toCuentaResponseDTO(cuentaGuardada);
    }

    public List<CuentaResponseDTO> buscarTodasLasCuentas() {
        return mapper.toCuentaResponseDTO(repoCuenta.findAll());
    }

    public List<CuentaResponseDTO> buscarCuentasPorDNI(String dni) {
        return mapper.toCuentaResponseDTO(repoCuenta.findByDni(dni));
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

    public BigDecimal verSaldo(Long numeroCuenta) {
        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return cuenta.getSaldo();
    }




    /*@Transactional
    public void actualizarMontoEnCuenta(Long numeroCuenta, Double monto) {
        if(monto == null || monto <= 0 ){
            throw new RuntimeException("Monto inválido");
        }

        Cuenta cuenta = repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        repoCuenta.save(cuenta);
    }*/



}
