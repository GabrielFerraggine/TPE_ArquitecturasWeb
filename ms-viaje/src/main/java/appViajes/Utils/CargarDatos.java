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
    private final ViajeRepository repoViaje;
    private final PausaRepository repoPausa;
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

            // Limpiar en orden inverso por las relaciones FK
            repoPausa.deleteAllInBatch();
            repoViaje.deleteAllInBatch();
            repoParada.deleteAllInBatch();

            // Cargar en orden correcto
            cargarParadas();
            cargarViajes();
            cargarPausas();

            System.out.println("Carga de datos completada exitosamente");

        } catch (Exception e) {
            System.err.println("Error cargando datos CSV: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en carga de datos", e);
        }
    }

    private void cargarParadas() {
        try {
            File paradasCSV = ResourceUtils.getFile("ms-viaje/src/main/resources/BDData/paradas.csv");
            List<Parada> paradas = new ArrayList<>();

            try (FileReader reader = new FileReader(paradasCSV);
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                for (CSVRecord record : csvParser) {
                    Parada parada = new Parada();
                    // NO establecer ID - dejar que se genere automáticamente
                    // parada.setId(Long.parseLong(record.get("id"))); // COMENTA ESTA LÍNEA

                    parada.setNombre(record.get("nombre"));
                    parada.setLatitud(Double.parseDouble(record.get("latitud")));
                    parada.setLongitud(Double.parseDouble(record.get("longitud")));
                    parada.setActiva(Boolean.parseBoolean(record.get("activa")));

                    if (record.isMapped("radio_permitido_metros") && !record.get("radio_permitido_metros").isEmpty()) {
                        parada.setRadioPermitidoMetros(Double.parseDouble(record.get("radio_permitido_metros")));
                    } else {
                        parada.setRadioPermitidoMetros(50.0);
                    }

                    paradas.add(parada);
                }
            }

            // Usar saveAll en lugar de EntityManager para evitar problemas de transacción
            if (!paradas.isEmpty()) {
                List<Parada> paradasGuardadas = repoParada.saveAll(paradas);
                System.out.println("Paradas cargadas: " + paradasGuardadas.size());
            }

        } catch (Exception e) {
            System.err.println("Error cargando paradas: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en carga de paradas", e);
        }
    }

    private void cargarViajes() {
        try {
            File viajesCSV = ResourceUtils.getFile("ms-viaje/src/main/resources/BDData/viajes.csv");
            List<Viaje> viajes = new ArrayList<>();

            try (FileReader reader = new FileReader(viajesCSV);
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                for (CSVRecord record : csvParser) {
                    Viaje viaje = new Viaje();
                    // NO establecer ID - dejar que se genere automáticamente
                    // viaje.setId(Long.parseLong(record.get("id"))); // COMENTA ESTA LÍNEA

                    viaje.setIdMonopatin(Long.parseLong(record.get("id_monopatin")));
                    viaje.setIdUsuario(Long.parseLong(record.get("id_usuario")));
                    viaje.setIdCuenta(Long.parseLong(record.get("id_cuenta")));
                    viaje.setFechaHoraInicio(parsearFecha(record.get("fecha_hora_inicio")));

                    if (record.isMapped("fecha_hora_fin") && !record.get("fecha_hora_fin").isEmpty()) {
                        viaje.setFechaHoraFin(parsearFecha(record.get("fecha_hora_fin")));
                    }

                    if (record.isMapped("km_recorridos") && !record.get("km_recorridos").isEmpty()) {
                        viaje.setKmRecorridos(Double.parseDouble(record.get("km_recorridos")));
                    } else {
                        viaje.setKmRecorridos(0.0);
                    }

                    if (record.isMapped("taifa") && !record.get("taifa").isEmpty()) {
                        viaje.setTaifa(Double.parseDouble(record.get("taifa")));
                    } else {
                        viaje.setTaifa(0.0);
                    }

                    viaje.setParadaInicio(Long.parseLong(record.get("parada_inicio")));

                    if (record.isMapped("parada_final") && !record.get("parada_final").isEmpty()) {
                        viaje.setParadaFinal(Long.parseLong(record.get("parada_final")));
                    }

                    // Configurar el estado
                    String estado = record.get("estado");
                    if (estado != null && !estado.isEmpty()) {
                        switch (estado.toUpperCase()) {
                            case "EN_CURSO":
                                viaje.setEstado(Viaje.EstadoViaje.EN_CURSO);
                                break;
                            case "PAUSADO":
                                viaje.setEstado(Viaje.EstadoViaje.PAUSADO);
                                break;
                            case "FINALIZADO":
                                viaje.setEstado(Viaje.EstadoViaje.FINALIZADO);
                                break;
                            default:
                                // Determinar estado basado en fecha_hora_fin
                                if (viaje.getFechaHoraFin() != null) {
                                    viaje.setEstado(Viaje.EstadoViaje.FINALIZADO);
                                } else {
                                    viaje.setEstado(Viaje.EstadoViaje.EN_CURSO);
                                }
                        }
                    } else {
                        // Si no hay estado, determinarlo por fecha_hora_fin
                        if (viaje.getFechaHoraFin() != null) {
                            viaje.setEstado(Viaje.EstadoViaje.FINALIZADO);
                        } else {
                            viaje.setEstado(Viaje.EstadoViaje.EN_CURSO);
                        }
                    }

                    viaje.setPausas(new ArrayList<>()); // Inicializar lista vacía
                    viajes.add(viaje);
                }
            }

            // Usar saveAll en lugar de EntityManager
            if (!viajes.isEmpty()) {
                List<Viaje> viajesGuardados = repoViaje.saveAll(viajes);
                System.out.println("Viajes cargados: " + viajesGuardados.size());
            }

        } catch (Exception e) {
            System.err.println("Error al cargar los viajes desde CSV: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en carga de viajes", e);
        }
    }

    private void cargarPausas() {
        try {
            File pausasCSV = ResourceUtils.getFile("ms-viaje/src/main/resources/BDData/pausas.csv");
            List<Pausa> pausas = new ArrayList<>();

            try (FileReader reader = new FileReader(pausasCSV);
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                for (CSVRecord record : csvParser) {
                    Pausa pausa = new Pausa();
                    // NO establecer ID - dejar que se genere automáticamente
                    // pausa.setId(Long.parseLong(record.get("id"))); // COMENTA ESTA LÍNEA

                    pausa.setViajeId(Long.parseLong(record.get("viaje_id")));
                    pausa.setFechaHoraInicio(parsearFecha(record.get("fecha_hora_inicio")));

                    if (record.isMapped("fecha_hora_fin") && !record.get("fecha_hora_fin").isEmpty()) {
                        pausa.setFechaHoraFin(parsearFecha(record.get("fecha_hora_fin")));
                    }

                    if (record.isMapped("pausa_extendida") && !record.get("pausa_extendida").isEmpty()) {
                        pausa.setPausaExtendida(Boolean.parseBoolean(record.get("pausa_extendida")));
                    } else {
                        pausa.setPausaExtendida(false);
                    }

                    pausas.add(pausa);
                }
            }

            // Usar saveAll en lugar de EntityManager
            if (!pausas.isEmpty()) {
                List<Pausa> pausasGuardadas = repoPausa.saveAll(pausas);
                System.out.println("Pausas cargadas: " + pausasGuardadas.size());
            }

        } catch (Exception e) {
            System.err.println("Error cargando pausas: " + e.getMessage());
            e.printStackTrace();
        }
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