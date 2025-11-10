package Utils;

import entity.Parada;
import entity.Viaje;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import repository.ParadaRepository;
import repository.PausaRepository;
import repository.ViajeRepository;

@Service
public class CargarDatos {
    private final ViajeRepository repoViaje;
    private final PausaRepository repoPausa;
    private final ParadaRepository repoParada;

    @Autowired
    public CargarDatos(ViajeRepository repoViaje, PausaRepository repoPausa, ParadaRepository repoParada) {
        this.repoParada = repoParada;
        this.repoPausa = repoPausa;
        this.repoViaje = repoViaje;
    }

    public void cargarDatosCSV() throws IOException {
        cargarParadas();
        cargarViajes();
        cargarPausas();
    }

    private void cargarParadas() throws IOException {
        File paradasCSV = ResourceUtils.getFile("src/main/resources/BDData/paradas.csv");
        int contador = 0;


        try (FileReader reader = new FileReader(paradasCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                Parada parada = new Parada();
                parada.setId(Long.parseLong(record.get("id")));
                parada.setNombre(record.get("nombre"));
                parada.setLatitud(Double.parseDouble(record.get("latitud")));
                parada.setLongitud(Double.parseDouble(record.get("longitud")));
                parada.setActiva(Boolean.parseBoolean(record.get("activa")));

                if (record.isMapped("radio_permitido_metros")) {
                    parada.setRadioPermitidoMetros(Double.parseDouble(record.get("radio_permitido_metros")));
                } else {
                    parada.setRadioPermitidoMetros(50.0);
                }

                repoParada.save(parada);
                contador++;
            }

            System.out.println(repoParada.count() + " paradas cargadas");
        } catch (Exception e) {
            System.out.println("Error cargando paradas: " + e.getMessage());
        }

    }
    private void cargarPausas() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("src/main/resources/BDData/pausas.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Saltar encabezado
                }

                String[] datos = line.split(",");
                if (datos.length >= 4) {
                    entity.Pausa pausa = new entity.Pausa();
                    pausa.setId(Long.parseLong(datos[0]));
                    pausa.setViajeId(Long.parseLong(datos[1]));
                    pausa.setFechaHoraInicio(LocalDateTime.parse(datos[2]));
                    pausa.setFechaHoraFin(LocalDateTime.parse(datos[3]));

                    repoPausa.save(pausa);
                }
            }
            System.out.println(repoPausa.count() + " pausas cargadas");
        } catch (Exception e) {
            System.out.println("Error cargando pausas: " + e.getMessage());
        }
    }

    private void cargarViajes() throws IOException {
        File viajesCSV = ResourceUtils.getFile("src/main/resources/BDData/viajes.csv");
        int contador = 0;

        try (FileReader reader = new FileReader(viajesCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                Viaje viaje = new Viaje();
                viaje.setId(Long.parseLong(record.get("id")));
                viaje.setIdMonopatin(Long.parseLong(record.get("id_monopatin")));
                viaje.setIdUsuario(Long.parseLong(record.get("id_usuario")));
                viaje.setIdCuenta(Long.parseLong(record.get("id_cuenta")));
                viaje.setFechaHoraInicio(parseLocalDateTime(record.get("fecha_hora_inicio")));

                if (record.isMapped("fecha_hora_fin") && !record.get("fecha_hora_fin").isEmpty()) {
                    viaje.setFechaHoraFin(parseLocalDateTime(record.get("fecha_hora_fin")));
                }

                if (record.isMapped("km_recorridos") && !record.get("km_recorridos").isEmpty()) {
                    viaje.setKmRecorridos(Double.parseDouble(record.get("km_recorridos")));
                }

                if (record.isMapped("taifa") && !record.get("taifa").isEmpty()) {
                    viaje.setTaifa(Double.parseDouble(record.get("taifa")));
                }

                viaje.setParadaInicio(Long.parseLong(record.get("parada_inicio")));

                if (record.isMapped("parada_final") && !record.get("parada_final").isEmpty()) {
                    viaje.setParadaFinal(Long.parseLong(record.get("parada_final")));
                }

                // Configurar el estado usando el enum
                String estado = record.get("estado");
                switch (estado) {
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
                        viaje.setEstado(Viaje.EstadoViaje.EN_CURSO);
                }

                repoViaje.save(viaje);
                contador++;
            }

            System.out.println("Se han cargado " + contador + " viajes desde el archivo CSV.");
        }
        catch (Exception e) {
            System.err.println("Error al cargar los viajes desde CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LocalDateTime parseLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr);
        } catch (Exception e) {
            System.err.println("Error al parsear fecha y hora: " + dateTimeStr + " - " + e.getMessage());
            return null;
        }
    }

}

