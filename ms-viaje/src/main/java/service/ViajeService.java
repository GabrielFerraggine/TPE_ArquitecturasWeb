package service;

import dto.ViajeCreateDTO;
import entity.Pausa;
import entity.Viaje;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ViajeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViajeService {
    private final ViajeRepository viajeRepository;

    @Transactional
    public Viaje inicirarViaje(ViajeCreateDTO viajeCreateDTO) {
        Optional<Viaje> viajeActivo = viajeRepository.findByIdMonopatinAndEstado(viajeCreateDTO.getIdMonopatin(), Viaje.EstadoViaje.EN_CURSO);

        if (viajeActivo.isPresent()) {
            throw new RuntimeException("El monopatín ya está en uso");
        }
        Viaje nuevoViaje = new Viaje(
                viajeCreateDTO.getIdMonopatin(),
                viajeCreateDTO.getIdUsuario(),
                LocalDateTime.now(),
                viajeCreateDTO.getParadaInicio()
        );

        return viajeRepository.save(nuevoViaje);
    }


    @Transactional
    public Viaje finalizarViaje(Long idViaje, Long paradaFinal, Double kmRecorridos, Double tarifa) {
        Viaje viajeActual = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        if (viajeActual.getEstado() != Viaje.EstadoViaje.EN_CURSO && viajeActual.getEstado() != Viaje.EstadoViaje.PAUSADO) {
            throw new RuntimeException("El viaje ya ha sido finalizado");
        }

        viajeActual.finalizarViaje(LocalDateTime.now(), paradaFinal, kmRecorridos);
        viajeActual.setTaifa(tarifa);
        return viajeRepository.save(viajeActual);
    }

    @Transactional
    public Viaje pausarViaje(Long idViaje){
        Viaje viajeActual = viajeRepository.findById(idViaje).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        if(viajeActual.getEstado() != Viaje.EstadoViaje.EN_CURSO) {
            throw new RuntimeException("El viaje no está en curso y no se puede pausar");
        }
        Pausa pausa = new Pausa();
        pausa.setFechaHoraInicio(LocalDateTime.now());
        pausa.setViaje(viajeActual);
        viajeActual.agregarPausa(pausa);
        viajeActual.setEstado(Viaje.EstadoViaje.PAUSADO);
        return viajeRepository.save(viajeActual);
    }
}
