package Aplicacion.service;

import Aplicacion.DTO.MonopatinDTO;
import Aplicacion.DTO.ReporteDTO;
import Aplicacion.entity.Estado;
import Aplicacion.entity.Monopatin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Aplicacion.repository.RepositoryMonopatin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ServiceMonopatin {

    @Autowired
    private RepositoryMonopatin repoMonopatin;

    @Transactional
    public Monopatin buscarMonopatinPorId(Long idMonopatin) {
        return repoMonopatin.buscarPorId(idMonopatin);
    }

    @Transactional(readOnly = true)
    public List<MonopatinDTO> traerTodos() {
        List<Monopatin> monopatines = repoMonopatin.traerTodos();
        return monopatines.stream().map(MonopatinDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public MonopatinDTO save(MonopatinDTO monopatinDto) {
        Monopatin monopatin = new Monopatin(monopatinDto);
        repoMonopatin.save(monopatin);
        return monopatinDto;
    }

    @Transactional
    public boolean setEstado(Long idMonopatin, Estado estado) {
        return (repoMonopatin.setEstado(idMonopatin, estado) == 1);
    }

    @Transactional(readOnly = true)
    public Estado getEstado(Long idMonopatin) {
        return repoMonopatin.getEstado(idMonopatin);
    }

    @Transactional
    public void borrarMonopatin(Monopatin monopatin) {
        repoMonopatin.delete(monopatin);
    }

    @Transactional(readOnly = true)
    public List<ReporteDTO> getReportePorKmRecorridos() {
        try {
            List<ReporteDTO> reporte = repoMonopatin.getReportePorKmRecorridos();

            if (reporte == null || reporte.isEmpty())
                return Collections.emptyList();

            return reporte;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por kilometros", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ReporteDTO> getReportePorKmYTiempoDePausas() {
        try {
            List<ReporteDTO> reporte = repoMonopatin.getReportePorKmYTiempoDePausas();

            if (reporte == null || reporte.isEmpty())
                return Collections.emptyList();

            return reporte;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por kilometros", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ReporteDTO> getReportePorTiempoDePausas() {
        try {
            List<ReporteDTO> reportes = repoMonopatin.getReportePorTiempoDePausas();
            if (reportes == null || reportes.isEmpty())
                return Collections.emptyList();

            return reportes;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por tiempo con pausas", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ReporteDTO> getReportePorTiempoDeUsoTotal() {
        try {
            List<ReporteDTO> reporte = repoMonopatin.getReportePorTiempoDeUsoTotal();

            if (reporte == null || reporte.isEmpty())
                return Collections.emptyList();

            return reporte;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por kilometros", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ReporteDTO> getReporteCompleto() {
        try {
            List<ReporteDTO> reporte = repoMonopatin.getReporteCompleto();

            if (reporte == null || reporte.isEmpty())
                return Collections.emptyList();

            return reporte;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por kilometros", e);
        }
    }

    @Transactional
    public boolean finalizarRecorrido(Long idMonopatin, double kmRecorridos, int tiempoDeUsoTotal, int tiempoDePausas) {
        Monopatin mpAUX = this.buscarMonopatinPorId(idMonopatin);
        mpAUX.setKmRecorridos(kmRecorridos + mpAUX.getKmRecorridos());
        mpAUX.setTiempoDeUsoTotal(tiempoDeUsoTotal + mpAUX.getTiempoDeUsoTotal());
        mpAUX.setTiempoDePausas(tiempoDePausas + mpAUX.getTiempoDePausas());
        repoMonopatin.setEstado(idMonopatin, Estado.LIBRE);
        int resultado = repoMonopatin.finalizarRecorrido(mpAUX.getIdMonopatin(), mpAUX.getKmRecorridos(), mpAUX.getTiempoDeUsoTotal(), mpAUX.getTiempoDePausas());
        return resultado != 0;
    }


    @Transactional(readOnly = true)
    public List<MonopatinDTO> getMonopatinesCercanos(double latitud, double longitud) {
        List<Monopatin> monopatines = repoMonopatin.getMonopatinesCercanos(latitud - 50.00, latitud + 50.00, longitud - 50.00, longitud + 50.00);
        return monopatines.stream().map(MonopatinDTO::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public double getLongitud(Long idMonopatin) {
        return repoMonopatin.getLongitud(idMonopatin);
    }

    @Transactional(readOnly = true)
    public double getLatitud(Long idMonopatin) {
        return repoMonopatin.getLatitud(idMonopatin);
    }
}

