package edu.tudai.microserviciofacturacion.utils;

import edu.tudai.microserviciofacturacion.entity.DetalleFactura;
import edu.tudai.microserviciofacturacion.entity.Factura;
import edu.tudai.microserviciofacturacion.entity.Tarifa;
import edu.tudai.microserviciofacturacion.repository.RepositoryDetalleFactura;
import edu.tudai.microserviciofacturacion.repository.RepositoryFactura;
import edu.tudai.microserviciofacturacion.repository.RepositoryTarifa;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class CargarDatos {

    private final RepositoryTarifa repoTarifa;
    private final RepositoryFactura repoFactura;
    private final RepositoryDetalleFactura repoDetalleFactura;

    @Autowired
    public CargarDatos(RepositoryTarifa repoTarifa, RepositoryFactura repoFactura, RepositoryDetalleFactura repoDetalleFactura) {
        this.repoTarifa = repoTarifa;
        this.repoFactura = repoFactura;
        this.repoDetalleFactura = repoDetalleFactura;
    }

    public void cargarDatosCSV() throws IOException {
        cargarTarifas();
        cargarFacturas();
        cargarDetallesFactura();
    }

    private void cargarTarifas() throws IOException {
        File tarifasCSV = ResourceUtils.getFile("classpath:DBData/tarifas.csv");
        try (FileReader reader = new FileReader(tarifasCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                Tarifa tarifa = new Tarifa();

                tarifa.setId(Long.parseLong(record.get("id")));
                tarifa.setTipo(Tarifa.TipoTarifa.valueOf(record.get("tipo").toUpperCase()));

                tarifa.setMonto(new BigDecimal(record.get("monto")));

                tarifa.setFechaInicio(LocalDate.parse(record.get("fechaInicio")));

                String fechaFinStr = record.get("fechaFin");
                if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                    tarifa.setFechaFin(LocalDate.parse(fechaFinStr));
                }

                repoTarifa.save(tarifa);
            }
        }
    }

    private void cargarFacturas() throws IOException {
        File facturasCSV = ResourceUtils.getFile("classpath:DBData/facturas.csv");
        try (FileReader reader = new FileReader(facturasCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                Factura factura = new Factura();

                factura.setId(Long.parseLong(record.get("id")));
                factura.setUsuarioId(Long.parseLong(record.get("usuarioId")));

                factura.setMontoTotal(new BigDecimal(record.get("montoTotal")));

                factura.setFechaEmision(LocalDate.parse(record.get("fechaEmision")));

                factura.setDetallesFactura(new ArrayList<>());

                repoFactura.save(factura);
            }
        }
    }

    private void cargarDetallesFactura() throws IOException {
        File detallesCSV = ResourceUtils.getFile("classpath:DBData/detalles_factura.csv");
        try (FileReader reader = new FileReader(detallesCSV);
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {

                Long facturaId = Long.parseLong(record.get("factura_id"));
                Factura factura = repoFactura.findById(facturaId)
                        .orElseThrow(() -> new IOException("Factura no encontrada con id: " + facturaId));

                DetalleFactura detalle = new DetalleFactura(
                        factura,
                        Long.parseLong(record.get("viajeId")),
                        new BigDecimal(record.get("tarifaBase")),
                        new BigDecimal(record.get("tarifaExtra")),

                        Long.parseLong(record.get("tiempoUso")),
                        Long.parseLong(record.get("tiempoPausado"))
                );

                detalle.setId(Long.parseLong(record.get("id")));

                detalle.setMontoCalculado(new BigDecimal(record.get("montoCalculado")));

                repoDetalleFactura.save(detalle);
            }
        }
    }
}