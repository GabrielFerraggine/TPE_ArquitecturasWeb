package service;

import entity.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RepositoryCuenta;

@Service
public class ServiceCuenta {

    @Autowired
    private RepositoryCuenta repoCuenta;

    public Cuenta buscarCuentaPorId(Long numeroCuenta) {
        return repoCuenta.findById(numeroCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }
}
