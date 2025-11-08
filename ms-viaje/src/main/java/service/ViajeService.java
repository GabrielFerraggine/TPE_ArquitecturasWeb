package service;


import dto.FinalizarViajeRequest;
import dto.IniciarViajeRequest;
import dto.PausaDTO;
import dto.ViajeDTO;
import entity.Viaje;
import org.springframework.stereotype.Service;
import repository.ViajeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    private ViajeRepository viajeRepository;

    public ViajeDTO inicirarViaje (IniciarViajeRequest request) {
        // Verificar si el usuario ya tiene un viaje activo
        Viaje viajeActico = viajeRepository.findViajeActivoByIdUsuario(request.getIdUsuario());
        if (viajeActico != null) {
            throw new RuntimeException("El usuario ya tiene un viaje activo");
        }
        Viaje monopatinActivo = viajeRepository.findViajeActivoByIdMonopatin(request.getIdMonopatin());
        if (monopatinActivo != null) {
            throw new RuntimeException("El monopatín ya está en uso");
        }
        Viaje nuevoViaje = new Viaje(
                request.getIdMonopatin(),
                request.getIdUsuario(),
                request.getIdCuenta(),
                LocalDateTime.now(),
                request.getParadaInicio()
        );

        Viaje viajeGuardado = viajeRepository.save(nuevoViaje);
        return mapToDTO(viajeGuardado);
    }

    public ViajeDTO finalizarViaje(FinalizarViajeRequest request){
        // Optional para manejar si no existe
        Optional<Viaje> viajeOptional = viajeRepository.findById(request.getIdViaje());
        if (viajeOptional.isEmpty()){ // Verificar si el viaje existe
            throw new RuntimeException("Viaje no encontrado");
        }
        Viaje viaje = viajeOptional.get();
        if (!viaje.getEstado().equals("EN_CURSO") && !viaje.getEstado().equals("PAUSADO")) {
            throw new RuntimeException("El viaje no está activo");
        }

        // ¿CALCULAR TARIFA?
        Double tarifa = calcularTarifa(viaje);

        viaje.finalizarViaje(LocalDateTime.now(), request.getParadaFinal(), request.getKmRecorridos(), tarifa);

        Viaje viajeFinalizado = viajeRepository.save(viaje);
        return mapToDTO(viajeFinalizado);
    }


    private ViajeDTO mapToDTO(Viaje viaje) {
        List<PausaDTO> pausasDTO = viaje.getPausas().stream()
                .map(p -> new PausaDTO(
                        p.getId(),
                        p.getFechaHoraInicio(),
                        p.getFechaHoraFin(),
                        p.getPausaExtendida()))
                .collect(Collectors.toList());

        return new ViajeDTO(
                viaje.getId(),
                viaje.getIdMonopatin(),
                viaje.getIdUsuario(),
                viaje.getIdCuenta(),
                viaje.getFechaHoraInicio(),
                viaje.getFechaHoraFin(),
                viaje.getKmRecorridos(),
                viaje.getTaifa(),
                viaje.getParadaInicio(),
                viaje.getParadaFinal(),
                viaje.getEstado().toString(),
                pausasDTO);
    }
}
