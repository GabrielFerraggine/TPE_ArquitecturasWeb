package Aplicacion.Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import Aplicacion.entity.Cuenta;
import Aplicacion.entity.TipoCuenta;
import Aplicacion.entity.Usuario;
import Aplicacion.repository.RepositoryCuenta;
import Aplicacion.repository.RepositoryUsuario;

@Service
public class CargarDatos {

    private final RepositoryCuenta repoCuenta;
    private final RepositoryUsuario repositoryUsuario;

    @Autowired
    public CargarDatos(RepositoryCuenta repoCuenta, RepositoryUsuario repositoryUsuario) {
        this.repoCuenta = repoCuenta;
        this.repositoryUsuario = repositoryUsuario;
    }

    @Transactional
    public void cargarDatosCSV() throws IOException, ParseException {
        cargarCuentas();
    }

    private void cargarCuentas() throws IOException, ParseException {
        File cuentasCSV = ResourceUtils.getFile("classpath:DBData/cuentas.csv");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try (FileReader reader = new FileReader(cuentasCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                Cuenta cuenta = new Cuenta();
                cuenta.setSaldo(new BigDecimal(record.get("saldo")));
                cuenta.setFechaAlta(sdf.parse(record.get("fechaAlta")));
                cuenta.setTipoCuenta(TipoCuenta.valueOf(record.get("tipoCuenta").toUpperCase()));
                cuenta.setIdCuentaMP(record.get("idCuentaMP"));
                cuenta.setActivo(Boolean.parseBoolean(record.get("activo")));

                cuenta = repoCuenta.save(cuenta);

                // Para terminar de crear la cuenta necesito saber si ya existen los usuarios o no
                String usuarios = record.get("usuarios");
                if (usuarios != null && !usuarios.isEmpty()) {
                    String[] dnis = usuarios.split("\\|");
                    for (String dni : dnis) {
                        final String dniFinal = dni.trim();
                        Usuario usuario = repositoryUsuario.findByDni(dniFinal)
                                .orElseGet(() -> {
                                    Usuario newUser = new Usuario();
                                    newUser.setDni(dniFinal);
                                    return repositoryUsuario.save(newUser);
                                });

                        boolean usuarioExisteEnCuenta = false;
                        for (Usuario u : cuenta.getUsuarios()) {
                            if (u.getDni().equals(usuario.getDni())) {
                                usuarioExisteEnCuenta = true;
                            }
                        }

                        if (!usuarioExisteEnCuenta) {
                            cuenta.getUsuarios().add(usuario);
                        }
                    }
                }

                // Guardo la cuenta con los usuarios existentes o nuevos
                repoCuenta.save(cuenta);
            }
        }
    }
}