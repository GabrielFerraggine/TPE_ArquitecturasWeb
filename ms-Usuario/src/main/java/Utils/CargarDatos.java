package Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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
    @Transactional
    public void init() {
        try {
            cargarDatosCSV();
            System.out.println("Datos cargados exitosamente desde CSV");
        } catch (Exception e) {
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

                // Primero cargar todos los usuarios sin relaciones complejas
                List<Usuario> usuarios = new ArrayList<>();
                Map<Long, Set<String>> cuentasUsuariosMap = new HashMap<>(); // cuentaId -> Set<usuarioIds>

                for (CSVRecord record : csvParser) {
                    Usuario usuario = crearUsuarioBasicoDesdeRecord(record);
                    usuarios.add(usuario);

                    // Recopilar información de relaciones para procesar después
                    String cuentasStr = record.get("cuentas");
                    if (cuentasStr != null && !cuentasStr.trim().isEmpty()) {
                        List<Long> idsCuentas = parsearListaLong(cuentasStr);
                        for (Long cuentaId : idsCuentas) {
                            cuentasUsuariosMap
                                    .computeIfAbsent(cuentaId, k -> new HashSet<>())
                                    .add(usuario.getIdUsuario());
                        }
                    }
                }

                // Guardar usuarios primero (sin las relaciones de cuentas)
                repoUsuario.saveAll(usuarios);
                System.out.println("Usuarios básicos guardados: " + usuarios.size());

                // Ahora establecer las relaciones ManyToMany
                establecerRelacionesCuentas(usuarios, cuentasUsuariosMap);

                System.out.println("Total usuarios cargados: " + repoUsuario.count());
            }
        } else {
            System.out.println("Los datos ya fueron cargados previamente");
        }
    }

    private Usuario crearUsuarioBasicoDesdeRecord(CSVRecord record) {
        Usuario usuario = new Usuario();

        usuario.setIdUsuario(record.get("idUsuario"));
        usuario.setNombre(record.get("nombre"));
        usuario.setApellido(record.get("apellido"));
        usuario.setNroTelefono(record.get("nroTelefono"));
        usuario.setMail(record.get("mail"));
        usuario.setRol(parsearRol(record.get("rol")));
        usuario.setLatitud(Double.parseDouble(record.get("latitud")));
        usuario.setLongitud(Double.parseDouble(record.get("longitud")));

        // Inicializar lista de cuentas vacía por ahora
        usuario.setCuentas(new ArrayList<>());

        // Procesar listas simples
        usuario.setMonopatines(parsearListaLong(record.get("monopatines")));
        usuario.setViajes(parsearListaLong(record.get("viajes")));

        return usuario;
    }

    private void establecerRelacionesCuentas(List<Usuario> usuarios, Map<Long, Set<String>> cuentasUsuariosMap) {
        // Crear un mapa de usuarios por ID para fácil acceso
        Map<String, Usuario> usuarioMap = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getIdUsuario, u -> u));

        // Para cada cuenta, crear la entidad y establecer relaciones
        for (Map.Entry<Long, Set<String>> entry : cuentasUsuariosMap.entrySet()) {
            Long cuentaId = entry.getKey();
            Set<String> usuarioIds = entry.getValue();

            Cuenta cuenta = new Cuenta();
            cuenta.setIdCuenta(cuentaId);

            // Establecer relaciones bidireccionales
            for (String usuarioId : usuarioIds) {
                Usuario usuario = usuarioMap.get(usuarioId);
                if (usuario != null) {
                    usuario.agregarCuenta(cuenta);
                }
            }
        }

        // Guardar usuarios actualizados con las relaciones
        repoUsuario.saveAll(usuarios);
        System.out.println("Relaciones de cuentas establecidas para " + cuentasUsuariosMap.size() + " cuentas únicas");
    }

    private Rol parsearRol(String rolStr) {
        try {
            String rolNormalizado = rolStr.trim().toUpperCase().replace(" ", "_");
            return Rol.valueOf(rolNormalizado);
        } catch (IllegalArgumentException e) {
            System.err.println("Rol no válido: " + rolStr + ", usando USUARIO por defecto");
            return Rol.USUARIO;
        }
    }

    private List<Long> parsearListaLong(String listaStr) {
        if (listaStr == null || listaStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return Arrays.stream(listaStr.split("\\|"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.err.println("Error parseando lista de longs: " + listaStr);
            return new ArrayList<>();
        }
    }
}