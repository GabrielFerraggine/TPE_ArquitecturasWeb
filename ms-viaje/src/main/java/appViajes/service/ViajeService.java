package appViajes.service;


import appViajes.dto.*;
import appViajes.entity.Pausa;
import appViajes.entity.Viaje;
import appViajes.feignClients.CuentaFeignClient;
import appViajes.feignClients.MonopatinFeignClient;
import appViajes.feignClients.UsuarioFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import appViajes.repository.ViajeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ParadaService paradaService;

    @Autowired
    private MonopatinFeignClient monopatinFeignClient;

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Autowired
    private CuentaFeignClient cuentaFeignClient;

    @Transactional(readOnly = true)
    public List<ViajeDTO> reportarViajes(){
        List<Viaje> viajes = viajeRepository.reportarViajes();
        return viajes.stream().map(ViajeDTO::new).collect(Collectors.toList());
    }

    public ViajeDTO iniciarViaje(IniciarViajeRequest request) {
        System.out.println("Service - Iniciando viaje con: " + request);

        // Validaciones
        if (request.getIdMonopatin() == null || request.getIdUsuario() == null ||
                request.getIdCuenta() == null || request.getParadaInicio() == null ||
                request.getParadaFinal() == null) {
            throw new RuntimeException("Todos los IDs (monopatin, usuario, cuenta, paradaInicio, paradaFinal) son obligatorios");
        }


        Viaje monopatinActivo = viajeRepository.findViajeActivoByMonopatin(request.getIdMonopatin());
        if (monopatinActivo != null) {
            throw new RuntimeException("El monopatín ya está en uso");
        }

        // Verificar que el monopatín esté disponible
        if (!monopatinFeignClient.verificarMonopatinActivo(request.getIdMonopatin())) {
            throw new RuntimeException("El monopatín no está disponible");
        }

        // Verificar que el monopatín esté en la parada de inicio
        boolean estaEnParadaInicio = paradaService.validarMonopatinEnParadaEspecifica(
                request.getIdMonopatin(),
                request.getParadaInicio()
        );

        if (!estaEnParadaInicio) {
            throw new RuntimeException("El monopatín no se encuentra en la parada de inicio especificada");
        }

        // Crear el viaje con ambas paradas
        Viaje nuevoViaje = new Viaje(
                request.getIdMonopatin(),
                request.getIdUsuario(),
                request.getIdCuenta(),
                LocalDateTime.now(),
                request.getParadaInicio(),
                request.getParadaFinal()
        );

        // Actualizar estado del monopatín
        try {
            monopatinFeignClient.actualizarEstadoMonopatin(request.getIdMonopatin(), "ENUSO");
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estado del monopatín: " + e.getMessage());
        }

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

        boolean estaEnParadaCorrecta = paradaService.validarMonopatinEnParadaEspecifica(
                viaje.getIdMonopatin(),
                request.getParadaFinal()
        );

        if (!estaEnParadaCorrecta) {
            throw new RuntimeException("El monopatín no se encuentra en la parada designada para finalizar");
        }

        //Calcular la tarifa?

        viaje.finalizarViaje(LocalDateTime.now(), request.getParadaFinal(),
                request.getKmRecorridos());

        monopatinFeignClient.finalizarRecorrido(viaje.getIdMonopatin(), viaje.getKmRecorridos(), viaje.duracionViaje(), viaje.duracionPausasTotales());

        Viaje viajeActualizado = viajeRepository.save(viaje);
        return mapToDTO(viajeActualizado);


        // /finalizarRecorrido/{idMonopatin}/{kmRecorridos}/{tiempoDeUsoTotal}/{tiempoDePausas}
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
        if (viaje.getEstado() != Viaje.EstadoViaje.EN_CURSO) {
            throw new RuntimeException("El viaje no está en curso");
        }

        Pausa pausa = new Pausa(LocalDateTime.now(), viaje.getId());
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


    public List<Map<String, Object>> obtenerMonopatinesConMasDeXViajesEnAnio(Long cantidadMinima, Integer anio) {
        return viajeRepository.findMonopatinesConMasDeXViajesEnAnio(cantidadMinima, anio);
    }

    @Transactional(readOnly = true)
    public Integer obtenerTiempoUsoMonopatines(Long idUsuario, LocalDateTime fechaInicio,
                                               LocalDateTime fechaFin, Boolean verCuentasRelacionadas) {

        // Validaciones básicas
        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        Integer tiempoTotal;

        if (Boolean.TRUE.equals(verCuentasRelacionadas)) {
            // Suma de TODOS los usuarios de las cuentas relacionadas
            tiempoTotal = viajeRepository.findTiempoUsoTotalPorCuentasRelacionadas(
                    idUsuario, fechaInicio, fechaFin);
        } else {
            // Solo el usuario específico
            tiempoTotal = viajeRepository.findTiempoUsoTotalPorUsuarioYPeriodo(
                    idUsuario, fechaInicio, fechaFin);
        }

        return tiempoTotal != null ? tiempoTotal : 0;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerTopUsuariosPorUso(LocalDateTime fechaInicio, LocalDateTime fechaFin, String tipoUsuario) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        List<Object[]> resultados;

        try {
            // Obtener cuentas premium del microservicio de usuarios
            List<Long> cuentasPremium = usuarioFeignClient.obtenerCuentasPremium();

            if (cuentasPremium == null) {
                cuentasPremium = Collections.emptyList(); // Evitar NullPointerException
            }

            switch (tipoUsuario != null ? tipoUsuario.toUpperCase() : "TODOS") {
                case "PREMIUM":
                    resultados = viajeRepository.findTopUsuariosPremiumPorUso(fechaInicio, fechaFin, cuentasPremium);
                    break;
                case "BASICO":
                    resultados = viajeRepository.findTopUsuariosBasicosPorUso(fechaInicio, fechaFin, cuentasPremium);
                    break;
                case "TODOS":
                default:
                    resultados = viajeRepository.findTopUsuariosPorUso(fechaInicio, fechaFin);
                    break;
            }

            return mapearResultadosTopUsuarios(resultados);

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener top usuarios: " + e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> mapearResultadosTopUsuarios(List<Object[]> resultados){
        List<Map<String, Object>> topUsuarios = new ArrayList<>();

        for (Object[] resultado : resultados){
            Map<String, Object> usuarioInfo = new HashMap<>();
            usuarioInfo.put("idUsuario", resultado[0]);
            usuarioInfo.put("cantidadViajes", resultado[1]);
            usuarioInfo.put("tiempoUsoTotalMinutos", resultado[2]);
            topUsuarios.add(usuarioInfo);
        }
        return topUsuarios;
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
