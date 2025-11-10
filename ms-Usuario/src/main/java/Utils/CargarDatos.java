package Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Entidades.Usuario;
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
            // Usar ClassPathResource en lugar de ResourceUtils.getFile
            org.springframework.core.io.Resource resource =
                    new org.springframework.core.io.ClassPathResource("DBData/usuario.csv");

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                for (CSVRecord record : csvParser) {
                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(Long.parseLong(record.get("idUsuario")));
                    usuario.setNombre(record.get("nombre"));
                    usuario.setApellido(record.get("apellido"));
                    usuario.setNroTelefono(record.get("nroTelefono"));
                    usuario.setMail(record.get("mail"));
                    usuario.setRol(Usuario.roles.valueOf(record.get("rol")));
                    usuario.setLatitud(Integer.parseInt(record.get("latitud")));
                    usuario.setLongitud(Integer.parseInt(record.get("longitud")));

                    repoUsuario.save(usuario);
                }
                System.out.println("Usuarios cargados: " + repoUsuario.count());
            }
        } else {
            System.out.println("Los datos ya fueron cargados previamente");
        }
    }
}