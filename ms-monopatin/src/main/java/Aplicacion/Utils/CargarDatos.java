package Aplicacion.Utils;

import Aplicacion.entity.Estado;
import Aplicacion.entity.Monopatin;
import Aplicacion.repository.RepositoryMonopatin;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CargarDatos {
    private final RepositoryMonopatin repoMonopatin;

    @Autowired
    public CargarDatos(RepositoryMonopatin repoMonopatin) {
        this.repoMonopatin = repoMonopatin;
    }



    public void cargarDatosCSV() throws IOException {
        cargarMonopatines();
    }

    private void cargarMonopatines() throws IOException {
        if (repoMonopatin.count() == 0) {
            // Usar ClassPathResource en lugar de ResourceUtils.getFile
            org.springframework.core.io.Resource resource =
                    new org.springframework.core.io.ClassPathResource("DBData/monopatin.csv");

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                 // Añade .withIgnoreSurroundingSpaces()
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreSurroundingSpaces().parse(reader)) {

                for (CSVRecord record : csvParser) {
                    Monopatin monopatin = new Monopatin();

                    monopatin.setIdMonopatin(Long.parseLong(record.get("idMonopatin")));
                    monopatin.setEstado(Estado.valueOf(record.get("estado").toUpperCase()));
                    monopatin.setLatitud(Double.parseDouble(record.get("latitud")));
                    monopatin.setLongitud(Double.parseDouble(record.get("longitud")));
                    monopatin.setKmRecorridos(Double.parseDouble(record.get("kmRecorridos")));
                    monopatin.setTiempoDeUsoTotal(Integer.parseInt(record.get("tiempoDeUsoTotal")));
                    monopatin.setTiempoDePausas(Integer.parseInt(record.get("tiempoDePausas")));

                    repoMonopatin.save(monopatin);
                }
                System.out.println("Monopatines cargados: " + repoMonopatin.count());
            }
        } else {
            System.out.println("Los datos ya fueron cargados previamente");
        }
    }
}