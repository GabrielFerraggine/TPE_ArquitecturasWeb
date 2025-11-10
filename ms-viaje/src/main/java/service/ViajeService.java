package service;


import dto.*;
import entity.Pausa;
import entity.Viaje;
import feignClients.MonopatinFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ViajeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ParadaService paradaService;

    @Autowired
    private MonopatinFeignClient monopatinFeignClient;


    public ViajeDTO iniciarViaje (IniciarViajeRequest request) {
        // Verificar si el usuario ya tiene un viaje activo
        Viaje viajeActico = viajeRepository.findViajeActivoByUsuario(request.getIdUsuario());
        if (viajeActico != null) {
            throw new RuntimeException("El usuario ya tiene un viaje activo");
        }
        Viaje monopatinActivo = viajeRepository.findViajeActivoByMonopatin(request.getIdMonopatin());
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

    public ViajeDTO finalizarViaje(FinalizarViajeRequest request) {
        Optional<Viaje> viajeOpt = viajeRepository.findById(request.getIdViaje());
        if (viajeOpt.isEmpty()) {
            throw new RuntimeException("Viaje no encontrado");
        }

        Viaje viaje = viajeOpt.get();
        validarEstadoParaFinalizar(viaje);

        paradaService.validarMonopatinEnParadaEspecifica(
                viaje.getIdMonopatin(),
                request.getParadaFinal()
        );

        //Calcular la tarifa?

        Viaje viajeActualizado = viajeRepository.save(viaje);
        return mapToDTO(viajeActualizado);
    }

    private void validarEstadoParaFinalizar(Viaje viaje) {
        if (viaje.getEstado() != Viaje.EstadoViaje.EN_CURSO &&
            viaje.getEstado() != Viaje.EstadoViaje.PAUSADO) {
            throw new RuntimeException("El viaje no está activo");
        }
    }


    public ViajeDTO pausarViaje(PausaRequest request){
        Optional<Viaje> viajeOptional = viajeRepository.findById(request.getIdViaje());
        if (viajeOptional.isEmpty()){ // Verificar si el viaje existe
            throw new RuntimeException("Viaje no encontrado");
        }
        Viaje viaje = viajeOptional.get();
        if (!viaje.getEstado().equals("EN_CURSO")) {
            throw new RuntimeException("El viaje no está en curso");
        }

        Pausa pausa = new Pausa(LocalDateTime.now());
        viaje.agregarPausa(pausa);
        viaje.setEstado(Viaje.EstadoViaje.PAUSADO);

        Viaje viajePausado = viajeRepository.save(viaje);
        return mapToDTO(viajePausado);
    }

    public ViajeDTO retomarViaje(PausaRequest request){
        Optional<Viaje> viajeOptional = viajeRepository.findById(request.getIdViaje());
        if (viajeOptional.isEmpty()) { // Verificar si el viaje existe
            throw new RuntimeException("Viaje no encontrado");
        }
        Viaje viaje = viajeOptional.get();
        if (!viaje.getEstado().equals("PAUSADO")) {
            throw new RuntimeException("El viaje no está pausado");
        }

        if (!viaje.getPausas().isEmpty()){
            Pausa ultimaPausa = viaje.getPausas().get(viaje.getPausas().size() - 1);
            if (ultimaPausa.getFechaHoraFin() == null) {
                ultimaPausa.finalizarPausa(LocalDateTime.now());
            }
        }

        viaje.setEstado(Viaje.EstadoViaje.EN_CURSO);
        Viaje viajeActualizado = viajeRepository.save(viaje);
        return mapToDTO(viajeActualizado);
    }

    public List<ViajeDTO> obtenerViajesPorUsuario(Long idUsuario){
        return viajeRepository.findByIdUsuario(idUsuario)
                .stream().map(
                        viaje->mapToDTO(viaje)
                )
                .collect(Collectors.toList());
    }

    public List<ViajeDTO> obtenerViajesPorCuenta(Long idCuenta){
        return viajeRepository.findByIdCuenta(idCuenta)
                .stream().map(
                        this::mapToDTO // viaje->mapToDTO(viaje)
                )
                .collect(Collectors.toList());
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
