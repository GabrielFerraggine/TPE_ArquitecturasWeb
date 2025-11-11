package appViajes.service;

import appViajes.entity.Parada;
import appViajes.feignClients.MonopatinFeignClient;
import appViajes.repository.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParadaService {

    @Autowired
    private ParadaRepository paradaRepository;

    @Autowired
    private MonopatinFeignClient monopatinFeignClient;

    public Boolean paradaValida(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) return false;
        return paradaRepository.paradaValida(latitud, longitud);
    }

    public boolean validarMonopatinEnParadaEspecifica(Long idMonopatin, Long paradaDesignadaId) {
        try {
            Parada paradaDesignada = paradaRepository.findByIdAndActivaTrue(paradaDesignadaId)
                    .orElse(null);

            if (paradaDesignada == null) {
                return false;
            }

            Double latitudMonopatin = monopatinFeignClient.obtenerLatitud(idMonopatin);
            Double longitudMonopatin = monopatinFeignClient.obtenerLongitud(idMonopatin);

            // Validar que se obtuvieron coordenadas
            if (latitudMonopatin == 0.0 || longitudMonopatin == 0.0) {
                return false;
            }

            Double distancia = calcularDistanciaMetros(
                    latitudMonopatin, longitudMonopatin,
                    paradaDesignada.getLatitud(), paradaDesignada.getLongitud()
            );

            return distancia <= paradaDesignada.getRadioPermitidoMetros();

        } catch (Exception e) {
            return false;
        }
    }

    private Double calcularDistanciaMetros(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}