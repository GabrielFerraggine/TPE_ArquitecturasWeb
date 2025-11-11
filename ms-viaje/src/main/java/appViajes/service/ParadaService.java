package appViajes.service;

import appViajes.dto.ParadaDTO;
import appViajes.entity.Parada;
import appViajes.feignClients.MonopatinFeignClient;
import appViajes.repository.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ParadaDTO crearParada(ParadaDTO paradaDTO) {
        if (paradaDTO.getNombre() == null || paradaDTO.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la parada es obligatorio");
        }
        if (paradaDTO.getLatitud() == null || paradaDTO.getLongitud() == null) {
            throw new RuntimeException("La latitud de la parada no puede ser nula o vacía.");
        }

        Parada parada = new Parada();
        parada.setNombre(paradaDTO.getNombre());
        parada.setLatitud(paradaDTO.getLatitud());
        parada.setLongitud(paradaDTO.getLongitud());
        parada.setActiva(paradaDTO.getActiva() != null ? paradaDTO.getActiva() : true);
        parada.setRadioPermitidoMetros(paradaDTO.getRadioPermitidoMetros() != null ? paradaDTO.getRadioPermitidoMetros() : 50.0);

        Parada paradaGuardada = paradaRepository.save(parada);
        return mapToDTO(paradaGuardada);
    }

    public List<ParadaDTO> obtenerTodasParadas() {
        List<Parada> paradas = paradaRepository.findAll();
        return paradas.stream().map(this::mapToDTO).toList();
        // resturn paradas.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ParadaDTO obtenerParadaPorId(Long id) {
        Parada parada = paradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parada no encontrada con id: " + id));
        return mapToDTO(parada);
    }

    public ParadaDTO actualizarParada(Long id, ParadaDTO paradaDTO) {
        Parada paradaExistente = paradaRepository.findById(id).orElseThrow(() -> new RuntimeException("Parada no encontrada con id: " + id));

        if (paradaDTO.getNombre() != null) {
            paradaExistente.setNombre(paradaDTO.getNombre());
        }
        if (paradaDTO.getLatitud() != null) {
            paradaExistente.setLatitud(paradaDTO.getLatitud());
        }
        if (paradaDTO.getLongitud() != null) {
            paradaExistente.setLongitud(paradaDTO.getLongitud());
        }
        if (paradaDTO.getActiva() != null) {
            paradaExistente.setActiva(paradaDTO.getActiva());
        }
        if (paradaDTO.getRadioPermitidoMetros() != null) {
            paradaExistente.setRadioPermitidoMetros(paradaDTO.getRadioPermitidoMetros());
        }

        Parada paradaActualizada = paradaRepository.save(paradaExistente);
        return mapToDTO(paradaActualizada);
    }

    public void borrarParada(Long id) {
        if(!paradaRepository.existsById(id)){
            throw new RuntimeException("Parada no encontrada con id: " + id);
        } else {
            paradaRepository.deleteById(id);
        }
    }

    private ParadaDTO mapToDTO(Parada parada) {
        ParadaDTO paradaDTO = new ParadaDTO();
        paradaDTO.setId(parada.getId());
        paradaDTO.setNombre(parada.getNombre());
        paradaDTO.setLatitud(parada.getLatitud());
        paradaDTO.setLongitud(parada.getLongitud());
        paradaDTO.setActiva(parada.getActiva());
        paradaDTO.setRadioPermitidoMetros(parada.getRadioPermitidoMetros());
        return paradaDTO;
    }
}