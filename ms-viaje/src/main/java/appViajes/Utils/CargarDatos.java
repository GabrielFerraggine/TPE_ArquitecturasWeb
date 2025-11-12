package appViajes.Utils;

import appViajes.entity.Parada;
import appViajes.entity.Pausa;
import appViajes.entity.Viaje;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import appViajes.repository.ParadaRepository;
import appViajes.repository.PausaRepository;
import appViajes.repository.ViajeRepository;

@Service
public class CargarDatos {
    @Autowired
    private final ViajeRepository repoViaje;
    @Autowired
    private final PausaRepository repoPausa;
    @Autowired
    private final ParadaRepository repoParada;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CargarDatos(ViajeRepository repoViaje, PausaRepository repoPausa, ParadaRepository repoParada) {
        this.repoParada = repoParada;
        this.repoPausa = repoPausa;
        this.repoViaje = repoViaje;
    }

    @Transactional
    public void cargarDatosCSV() {
        try {
            System.out.println("Iniciando carga de datos...");
            long startTime = System.currentTimeMillis();

            // Limpiar en orden inverso por las relaciones FK
            System.out.println("Limpiando datos anteriores...");
            repoPausa.deleteAllInBatch();
            repoViaje.deleteAllInBatch();
            repoParada.deleteAllInBatch();
            System.out.println("Datos limpiados");

            // Cargar en orden correcto
            cargarParadas();
            cargarViajes();
            cargarPausas();

            long endTime = System.currentTimeMillis();
            System.out.println("Carga de datos completada en " + (endTime - startTime) + " ms");

        } catch (Exception e) {
            System.err.println("Error cargando datos CSV: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en carga de datos", e);
        }
    }

    private void cargarParadas() {
        try {
            File paradasCSV = ResourceUtils.getFile("ms-viaje/src/main/resources/BDData/paradas.csv");

            try (FileReader reader = new FileReader(paradasCSV);
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                int batchSize = 50;
                int count = 0;
                List<Object[]> batchParams = new ArrayList<>();

                for (CSVRecord record : csvParser) {
                    Object[] params = new Object[]{
                            Long.parseLong(record.get("id")), // ID manual
                            record.get("nombre"),
                            Double.parseDouble(record.get("latitud")),
                            Double.parseDouble(record.get("longitud")),
                            Boolean.parseBoolean(record.get("activa")),
                            record.isMapped("radio_permitido_metros") && !record.get("radio_permitido_metros").isEmpty()
                                    ? Double.parseDouble(record.get("radio_permitido_metros")) : 50.0
                    };

                    batchParams.add(params);
                    count++;

                    if (batchParams.size() >= batchSize) {
                        ejecutarBatchParadas(batchParams);
                        batchParams.clear();
                        System.out.println("Procesadas " + count + " paradas...");
                    }
                }

                if (!batchParams.isEmpty()) {
                    ejecutarBatchParadas(batchParams);
                }

                System.out.println("Total paradas cargadas: " + count);

            } catch (Exception e) {
                System.err.println("Error cargando paradas: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error en carga de paradas", e);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de paradas no encontrado: " + e.getMessage());
            throw new RuntimeException("Archivo de paradas no encontrado", e);
        }
    }

    private void ejecutarBatchParadas(List<Object[]> batchParams) {
        String sql = "INSERT INTO paradas (id, nombre, latitud, longitud, activa, radio_permitido_metros) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        for (Object[] params : batchParams) {
            entityManager.createNativeQuery(sql)
                    .setParameter(1, params[0])
                    .setParameter(2, params[1])
                    .setParameter(3, params[2])
                    .setParameter(4, params[3])
                    .setParameter(5, params[4])
                    .setParameter(6, params[5])
                    .executeUpdate();
        }
        entityManager.flush();
    }

    private void cargarViajes() {
        try {
            File viajesCSV = ResourceUtils.getFile("ms-viaje/src/main/resources/BDData/viajes.csv");

            try (FileReader reader = new FileReader(viajesCSV);
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                int batchSize = 50;
                int count = 0;
                List<Object[]> batchParams = new ArrayList<>();

                for (CSVRecord record : csvParser) {
                    Object[] params = new Object[]{
                            Long.parseLong(record.get("id")), // ID manual
                            Long.parseLong(record.get("id_monopatin")),
                            Long.parseLong(record.get("id_usuario")),
                            Long.parseLong(record.get("id_cuenta")),
                            parsearFecha(record.get("fecha_hora_inicio")),
                            record.isMapped("fecha_hora_fin") && !record.get("fecha_hora_fin").isEmpty()
                                    ? parsearFecha(record.get("fecha_hora_fin")) : null,
                            record.isMapped("km_recorridos") && !record.get("km_recorridos").isEmpty()
                                    ? Double.parseDouble(record.get("km_recorridos")) : 0.0,
                            record.isMapped("taifa") && !record.get("taifa").isEmpty()
                                    ? Double.parseDouble(record.get("taifa")) : 0.0,
                            Long.parseLong(record.get("parada_inicio")),
                            record.isMapped("parada_final") && !record.get("parada_final").isEmpty()
                                    ? Long.parseLong(record.get("parada_final")) : null,
                            determinarEstado(record)
                    };

                    batchParams.add(params);
                    count++;

                    // Ejecutar por lotes
                    if (batchParams.size() >= batchSize) {
                        ejecutarBatchViajes(batchParams);
                        batchParams.clear();
                        System.out.println("Procesados " + count + " viajes...");
                    }
                }

                // Ejecutar último lote
                if (!batchParams.isEmpty()) {
                    ejecutarBatchViajes(batchParams);
                }

                System.out.println("Total viajes cargados: " + count);

            } catch (Exception e) {
                System.err.println("Error al cargar los viajes desde CSV: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error en carga de viajes", e);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de viajes no encontrado: " + e.getMessage());
            throw new RuntimeException("Archivo de viajes no encontrado", e);
        }
    }

    private void ejecutarBatchViajes(List<Object[]> batchParams) {
        String sql = "INSERT INTO viajes (id, id_monopatin, id_usuario, id_cuenta, fecha_hora_inicio, " +
                "fecha_hora_fin, km_recorridos, taifa, parada_inicio, parada_final, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (Object[] params : batchParams) {
            entityManager.createNativeQuery(sql)
                    .setParameter(1, params[0])
                    .setParameter(2, params[1])
                    .setParameter(3, params[2])
                    .setParameter(4, params[3])
                    .setParameter(5, params[4])
                    .setParameter(6, params[5])
                    .setParameter(7, params[6])
                    .setParameter(8, params[7])
                    .setParameter(9, params[8])
                    .setParameter(10, params[9])
                    .setParameter(11, params[10])
                    .executeUpdate();
        }
        entityManager.flush();
    }

    private String determinarEstado(CSVRecord record) {
        String estado = record.get("estado");
        if (estado != null && !estado.isEmpty()) {
            return estado.toUpperCase();
        }
        // Determinar por fecha_hora_fin
        if (record.isMapped("fecha_hora_fin") && !record.get("fecha_hora_fin").isEmpty()) {
            return "FINALIZADO";
        }
        return "EN_CURSO";
    }

    private void cargarPausas() {
        try {
            File pausasCSV = ResourceUtils.getFile("ms-viaje/src/main/resources/BDData/pausas.csv");

            try (FileReader reader = new FileReader(pausasCSV);
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                int batchSize = 50;
                int count = 0;
                List<Object[]> batchParams = new ArrayList<>();

                for (CSVRecord record : csvParser) {
                    Object[] params = new Object[]{
                            Long.parseLong(record.get("id")), // ID manual
                            Long.parseLong(record.get("viaje_id")),
                            parsearFecha(record.get("fecha_hora_inicio")),
                            record.isMapped("fecha_hora_fin") && !record.get("fecha_hora_fin").isEmpty()
                                    ? parsearFecha(record.get("fecha_hora_fin")) : null,
                            record.isMapped("pausa_extendida") && !record.get("pausa_extendida").isEmpty()
                                    ? Boolean.parseBoolean(record.get("pausa_extendida")) : false
                    };

                    batchParams.add(params);
                    count++;

                    if (batchParams.size() >= batchSize) {
                        ejecutarBatchPausas(batchParams);
                        batchParams.clear();
                        System.out.println("Procesadas " + count + " pausas...");
                    }
                }

                if (!batchParams.isEmpty()) {
                    ejecutarBatchPausas(batchParams);
                }

                System.out.println("Total pausas cargadas: " + count);

            } catch (Exception e) {
                System.err.println("Error cargando pausas: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error en carga de pausas", e);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de pausas no encontrado: " + e.getMessage());
            throw new RuntimeException("Archivo de pausas no encontrado", e);
        }
    }

    private void ejecutarBatchPausas(List<Object[]> batchParams) {
        String sql = "INSERT INTO pausas (id, viaje_id, fecha_hora_inicio, fecha_hora_fin, pausa_extendida) " +
                "VALUES (?, ?, ?, ?, ?)";

        for (Object[] params : batchParams) {
            entityManager.createNativeQuery(sql)
                    .setParameter(1, params[0])
                    .setParameter(2, params[1])
                    .setParameter(3, params[2])
                    .setParameter(4, params[3])
                    .setParameter(5, params[4])
                    .executeUpdate();
        }
        entityManager.flush();
    }

    private LocalDateTime parsearFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }
        try {
            // Probar formato con espacio
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(fechaStr, formatter);
        } catch (Exception e1) {
            try {
                // Probar formato ISO
                return LocalDateTime.parse(fechaStr);
            } catch (Exception e2) {
                System.err.println("No se pudo parsear la fecha: " + fechaStr);
                return null;
            }
        }
    }
}