package Aplicacion.service;

import Aplicacion.DTO.MonopatinDTO;
import Aplicacion.DTO.ReporteDTO;
import Aplicacion.entity.Estado;
import Aplicacion.entity.Monopatin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import Aplicacion.repository.RepositoryMonopatin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ServiceMonopatin {

    @Autowired
    private RepositoryMonopatin repoMonopatin;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Monopatin buscarMonopatinPorId(Long idMonopatin) {
        return repoMonopatin.findByIdMonopatin(idMonopatin);
    }

    public List<MonopatinDTO> traerTodos() {
        List<Monopatin> monopatines = repoMonopatin.traerTodos();
        return monopatines.stream()
                .map(MonopatinDTO::new)
                .collect(Collectors.toList());
    }


    public MonopatinDTO save(MonopatinDTO monopatinDto) {
        Monopatin monopatin = new Monopatin(monopatinDto);
        repoMonopatin.save(monopatin);
        return monopatinDto;
    }


    public boolean setEstado(Long idMonopatin, Estado estado) {
        Optional<Monopatin> oMonopatin = repoMonopatin.findById(idMonopatin);

        if (oMonopatin.isPresent()) {
            Monopatin monopatin = oMonopatin.get();
            monopatin.setEstado(estado);
            repoMonopatin.save(monopatin);

            return true;
        }
        return false;
    }

    public Estado getEstado(Long idMonopatin) {
        return repoMonopatin.findById(idMonopatin)
                .map(Monopatin::getEstado)
                .orElseThrow(() -> new RuntimeException("Error: Monopatín no encontrado con ID: " + idMonopatin));
    }


    public void borrarMonopatin(Monopatin monopatin) {
        repoMonopatin.delete(monopatin);
    }

    public List<ReporteDTO> getReportePorKmRecorridos() {
        try {
            List<Monopatin> monopatines = repoMonopatin.findAllDataParaReporteKm();

            if (monopatines.isEmpty()) {
                return Collections.emptyList();
            }

            return monopatines.stream()
                    .map(m -> new ReporteDTO(
                            m.getIdMonopatin(),
                            m.getKmRecorridos(),
                            0,
                            0
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por kilometros", e);
        }
    }

    public List<ReporteDTO> getReportePorKmYTiempoDePausas() {
        try {
            List<Monopatin> monopatines = repoMonopatin.findAllDataReportePausasyKm();

            if (monopatines.isEmpty()) {
                return Collections.emptyList();
            }

            return monopatines.stream()
                    .map(m -> new ReporteDTO(
                            m.getIdMonopatin(),
                            m.getKmRecorridos(),
                            0,
                            m.getTiempoDePausas()
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por kilometros y tiempos de pausa", e);
        }
    }

    public List<ReporteDTO> getReportePorTiempoDeUsoTotal() {
        try {
            List<Monopatin> monopatines = repoMonopatin.findAllDataReportePorTiempoDeUsoTotal();

            if (monopatines.isEmpty()) {
                return Collections.emptyList();
            }

            return monopatines.stream()
                    .map(m -> new ReporteDTO(
                            m.getIdMonopatin(),
                            0,
                            m.getTiempoDeUsoTotal(),
                            0
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por tiempo de uso total", e);
        }
    }

    public List<ReporteDTO> getReportePorTiempoDePausas() {
        try {
            List<Monopatin> monopatines = repoMonopatin.findAllDataReportePorTiempoDePausas();

            if (monopatines.isEmpty()) {
                return Collections.emptyList();
            }

            return monopatines.stream()
                    .map(m -> new ReporteDTO(
                            m.getIdMonopatin(),
                            0,
                            0,
                            m.getTiempoDePausas()
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de uso de monopatines por tiempo de pausas", e);
        }
    }

    public List<ReporteDTO> getReporteCompleto() {
        try {
            List<Monopatin> monopatines = repoMonopatin.findAllDataReporteCompleto();

            if (monopatines.isEmpty()) {
                return Collections.emptyList();
            }

            return monopatines.stream()
                    .map(m -> new ReporteDTO(
                            m.getIdMonopatin(),
                            m.getKmRecorridos(),
                            m.getTiempoDeUsoTotal(),
                            m.getTiempoDePausas()
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte completo de monopatines", e);
        }
    }


    public boolean finalizarRecorrido(Long idMonopatin, double kmRecorridos,
                                      int tiempoDeUsoTotal, int tiempoDePausas) {
        Monopatin monopatin = buscarMonopatinPorId(idMonopatin);

        if (monopatin == null) {
            return false;
        }

        double nuevosKm = monopatin.getKmRecorridos() + kmRecorridos;
        int nuevoTiempoUso = monopatin.getTiempoDeUsoTotal() + tiempoDeUsoTotal;
        int nuevoTiempoPausas = monopatin.getTiempoDePausas() + tiempoDePausas;

        monopatin.setKmRecorridos(nuevosKm);
        monopatin.setTiempoDeUsoTotal(nuevoTiempoUso);
        monopatin.setTiempoDePausas(nuevoTiempoPausas);
        monopatin.setEstado(Estado.LIBRE);

        repoMonopatin.save(monopatin);

        return true;
    }


    public List<MonopatinDTO> getMonopatinesCercanos(double latitud, double longitud) {
        List<Monopatin> monopatines = repoMonopatin.getMonopatinesCercanos(
                latitud - 50.00,
                latitud + 50.00,
                longitud - 50.00,
                longitud + 50.00
        );

        return monopatines.stream()
                .map(MonopatinDTO::new)
                .collect(Collectors.toList());
    }


    public double getLongitud(Long idMonopatin) {
        Monopatin m = repoMonopatin.getLongitud(idMonopatin);

        if (m == null) {
            throw new RuntimeException("Monopatín no encontrado con ID: " + idMonopatin);
        }

        return m.getLongitud();
    }

    public double getLatitud(Long idMonopatin) {
        Monopatin m = repoMonopatin.getLatitud(idMonopatin);

        if (m == null) {
            throw new RuntimeException("Monopatín no encontrado con ID: " + idMonopatin);
        }

        return m.getLatitud();
    }
}

