package Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Entidades.*;
import Repository.RepositoryUsuario;

@Service
public class CargarDatos {
    private final RepositoryUsuario repoUsuario;

    @Autowired
    public CargarDatos(RepositoryUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
    }

    @PostConstruct
    public void init() {
        try {
            cargarDatosCSV();
            System.out.println("Datos cargados exitosamente desde CSV");
        } catch (IOException e) {
            System.err.println("Error al cargar datos desde CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargarDatosCSV() throws IOException {
        cargarUsuarios();
    }

    private void cargarUsuarios() throws IOException {
        if (repoUsuario.count() == 0) {
            org.springframework.core.io.Resource resource =
                    new org.springframework.core.io.ClassPathResource("DBData/usuario.csv");

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                for (CSVRecord record : csvParser) {
                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(record.get("idUsuario"));
                    usuario.setNombre(record.get("nombre"));
                    usuario.setApellido(record.get("apellido"));
                    usuario.setNroTelefono(record.get("nroTelefono"));
                    usuario.setMail(record.get("mail"));
                    usuario.setRol(Rol.valueOf(record.get("rol")));
                    usuario.setLatitud(Double.parseDouble(record.get("latitud")));
                    usuario.setLongitud(Double.parseDouble(record.get("longitud")));
                    usuario.setCuentas(parsearListaCuentas(record.get("cuentas")));
                    usuario.setMonopatines(parsearListaLong(record.get("monopatines")));
                    usuario.setViajes(parsearListaLong(record.get("viajes")));

                    repoUsuario.save(usuario);
                    System.out.println("Usuario creado: " + usuario.getNombre() + " con " +
                            usuario.getCuentas().size() + " cuentas");
                }
                System.out.println("Total usuarios cargados: " + repoUsuario.count());
            }
        } else {
            System.out.println("Los datos ya fueron cargados previamente");
        }
    }

    private List<Cuenta> parsearListaCuentas(String cuentasStr) {
        if (cuentasStr == null || cuentasStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(cuentasStr.split("\\|"))
                .map(String::trim)
                .map(Long::parseLong)
                .map(id -> {
                    Cuenta cuenta = new Cuenta();
                    cuenta.setIdCuenta(id);
                    // Si la entidad Cuenta tiene más campos, puedes inicializarlos aquí
                    return cuenta;
                })
                .collect(Collectors.toList());
    }

    private List<Long> parsearListaLong(String listaStr) {
        if (listaStr == null || listaStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(listaStr.split("\\|"))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}