package Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import Entidades.Usuario;
import Repository.RepositoryUsuario;

@Service
public class CargarDatos {
    private final RepositoryUsuario repoUsuario;

    @Autowired
    public CargarDatos(RepositoryUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
    }

    public void cargarDatosCSV() throws IOException {
        cargarUsuarios();
    }

    private void cargarUsuarios() throws IOException {
        File usuariosCSV = ResourceUtils.getFile("classpath:DBData/usuarios.csv");

        try (FileReader reader = new FileReader(usuariosCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                Usuario usuario = new Usuario();

                usuario.setIdUsuario(Long.parseLong(record.get("idUsuario"))); // se toma del CSV
                usuario.setNombre(record.get("nombre"));
                usuario.setApellido(record.get("apellido"));
                usuario.setNroTelefono(record.get("nroTelefono"));
                usuario.setMail(record.get("mail"));
                usuario.setRol(Usuario.roles.valueOf(record.get("rol").toUpperCase()));
                usuario.setLatitud(Integer.parseInt(record.get("latitud")));
                usuario.setLongitud(Integer.parseInt(record.get("longitud")));

                repoUsuario.save(usuario);
            }
        }
    }
}
