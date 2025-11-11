package Servicio;

import Entidades.Usuario;
import Modelos.*;
import Repository.RepositoryUsuario;
import DTO.DTOUsuario;
import feignClients.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feignClients.FeignClientFacturacion;
import java.time.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioUsuario {

    @Autowired
    private RepositoryUsuario repoUsuario;

    //@Autowired
    //private FeignClientCuenta feignCuenta;

    //@Autowired
    //private FeignClientMonopatin feignMonopatin;

    @Autowired
    private FeignClientViaje feignViaje;

    @Autowired
    private FeignClientFacturacion feignFacturacion;

    public DTOUsuario toDTO(Usuario u) {
        DTOUsuario dtoUsuario = new DTOUsuario(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
       //         u.getCuentas(),
                u.getMail(),
                u.getRol(),
                u.getLatitud(),
                u.getLongitud()
                //,u.getMonopatines(),
        //     u.getViajes()
        );
        return dtoUsuario;
    }

    /*=========================Metodos de los FeignClient================================*/
    /*g. Como usuario quiero un listado de los monopatines cercanos a mi zona,
    para poder encontrar un monopatín cerca de mi ubicación*/
    /*@Transactional(readOnly = true)
    public List<Monopatin> obtenerMonopatinesCercanos(int latitud, int longitud) throws Exception {
        try {
            return feignMonopatin.obtenerMonopatinesCercanos(latitud, longitud);
        } catch (Exception e) {
            throw new Exception("Error al obtener monopatines cercanos: " + e.getMessage());
        }
    }*/

    //Obtener una cuenta de un usuario
    /*@Transactional(readOnly = true)
    public Cuenta obtenerCuentaUsuario(Long idCuenta) throws Exception{
        try {
            return feignCuenta.obtenerCuentaUsuario(idCuenta);
        } catch (Exception e) {
            throw new Exception("No se pudo obtener la cuenta: " + e.getMessage());
        }
    }*/

    //Obtener todas las cuentas de un usuario
    /*@Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasUsuarios(Long idCuenta) throws Exception {
        try {
            return feignCuenta.obtenerCuentasUsuario(idCuenta);
        } catch (Exception e) {
            throw new Exception("no se pudo obtener todas las cuentas: " + e.getMessage());
        }
    }*/

    //Obtener un viaje de un usuario
    /*@Transactional(readOnly = true)
    public Viaje obtenerViaje(Long idViaje, Long idUsuario) throws Exception {
        try {
            return feignViaje.obtenerViaje(idViaje, idUsuario);
        } catch (Exception e) {
            throw new Exception("No se pudo obtener el viaje solicitado: " + e.getMessage());
        }
    }*/

    //Obtener todos los viajes de un usuario
    /*@Transactional(readOnly = true)
    public List<Viaje> obtenerViajesUsuario(Long idUsuario) throws Exception {
        try {
            return feignViaje.obtenerViajesUsuario(idUsuario);
        } catch (Exception e) {
            throw new Exception("No se pudo obtener los viajes solicitados: " + e.getMessage());
        }
    }*/

    //Agregar un nuevo viaje
    /*@Transactional
    public Viaje agregarViaje(Viaje v) throws Exception{
        try {
            return feignViaje.save(v);
        } catch(Exception e) {
            throw new Exception("Error al crear un nuevo viaje: " + e.getMessage());
        }
    }*/

    @Transactional(readOnly = true)
    public List<Long> obtenerMonopatinesMasUsados(int anioDeseado, Long cantidadMinimaViajes) throws Exception {
        try {
            return feignViaje.obtenerMonopatinesMasUsados(anioDeseado, cantidadMinimaViajes);
        } catch (Exception e) {
            throw new Exception("Error al obtener los monopatines mas usados en el año: " + anioDeseado
                    + " con una cantidad minima de viajes de: " + cantidadMinimaViajes);
        }
    }

    /*@Transactional(readOnly = true)
    public Double obtenerTotalFacturado(int anioDeseado, int mesDeseadoInicial, int mesDeseadoFinal) throws Exception {
        try {
            return feignFacturacion.obtenerTotalFacturado(anioDeseado, mesDeseadoInicial, mesDeseadoFinal);
        } catch (Exception e) {
            throw new Exception("Error al obtener el total facturado en el año: " + anioDeseado + " en el intervalo de meses: "
                    + mesDeseadoInicial + " y " +mesDeseadoFinal);
        }
    }*/

    @Transactional
    public void ajustarPreciosTarifas(BigDecimal nuevaTarifaBase, BigDecimal nuevaTarifaExtra, LocalDate fechaInicio) throws Exception {
        try {
            feignFacturacion.ajustarPreciosTarifas(nuevaTarifaBase, nuevaTarifaExtra, fechaInicio);
        } catch (Exception e) {
            throw new Exception("Error al ajustar los precios de las tarifas: " + e.getMessage());
        }
    }

    /*==================================Metodos propios==================================*/
    //Obtener un usuario
    @Transactional(readOnly = true)
    public DTOUsuario obtenerUsuario(Long id) throws Exception{
        try {
            Usuario usuario = repoUsuario.obtenerUsuario(id);
            DTOUsuario dtoUsuario = this.toDTO(usuario);
            return dtoUsuario;
        } catch (Exception e) {
            throw new Exception("Error al obtener usuario con la id: " + id + " " + e.getMessage());
        }
    }

    //Obtener todos los usuarios
    @Transactional(readOnly = true)
    public List<DTOUsuario> obtenerUsuarios() throws Exception {
        try {
            List<Usuario> usuarios = repoUsuario.obtenerUsuarios();
            List<DTOUsuario> dtoUsuarios = new ArrayList<>();
            for (Usuario u : usuarios) {
                dtoUsuarios.add(this.toDTO(u));
            }
            return dtoUsuarios;
        } catch (Exception e) {
            throw new Exception("No se pudo obtener todos los usuarios " + e.getMessage());
        }
    }

    //Obtener todos los usuarios habilitados
    /*@Transactional(readOnly = true)
    public List<DTOUsuario> obtenerUsuariosHabilitados() throws Exception {
        try {
            List<Usuario> usuarios = repoUsuario.obtenerHabilitados();
            List<DTOUsuario> dtoUsuarios = new ArrayList<>();
            for (Usuario u: usuarios) {
                dtoUsuarios.add(this.toDTO(u));
            }
            return dtoUsuarios;
        } catch(Exception e) {
            throw new Exception("no se pudo obtener los usuarios habilitados " + e.getMessage());
        }
    }*/

    //Obtener todos los usuarios deshabilitados
    /*@Transactional(readOnly = true)
    public List<DTOUsuario> obtenerUsuariosDeshabilitados() throws Exception {
        try {
            List<Usuario> usuarios = repoUsuario.obtenerDeshabilitados();
            List<DTOUsuario> dtoUsuarios = new ArrayList<>();
            for (Usuario u: usuarios) {
                dtoUsuarios.add(this.toDTO(u));
            }
            return dtoUsuarios;
        } catch(Exception e) {
            throw new Exception("no se pudo obtener los usuarios habilitados " + e.getMessage());
        }
    }*/


    //Dar de alta un usuario
    @Transactional
    public DTOUsuario agregarUsuario(Usuario u) throws Exception{
        try {
            Usuario usuarioGuardado = repoUsuario.save(u);
            DTOUsuario dtoUsuario = this.toDTO(usuarioGuardado);
            return dtoUsuario;

        } catch(Exception e) {
            throw new Exception("Error al crear un nuevo usuario " + e.getMessage());
        }
    }

    //Dar de baja un usuario
    @Transactional
    public Boolean eliminarUsuario(Long id) throws Exception{
        try {
            if(repoUsuario.existsById(id)) {
                repoUsuario.deleteById(id);
                return true;
            }
             else {
                 throw new Exception("El usuario con la id: " + id + " no existe ");
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar el usuario con la id: " + id);
        }
    }


    //Modificar un usuario (funciona como un patch)
    @Transactional
    public DTOUsuario actualizarUsuario(Long id, Usuario usuarioActualizado) throws Exception {
        try {
            Usuario usuarioExistente = repoUsuario.findById(id)
                    .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));
            actualizarCamposUsuario(usuarioExistente, usuarioActualizado);
            Usuario usuarioGuardado = repoUsuario.save(usuarioExistente);
            return this.toDTO(usuarioGuardado);

        } catch (Exception e) {
            throw new Exception("Error al actualizar el usuario con id=" + id + ": " + e.getMessage());
        }
    }

    //El metodo comprueba los campos para no tener que realizar un update con un objeto con todos los parametros
    private void actualizarCamposUsuario(Usuario existente, Usuario actualizado) {
        if (actualizado.getNombre() != null) {
            existente.setNombre(actualizado.getNombre());
        }
        if (actualizado.getApellido() != null) {
            existente.setApellido(actualizado.getApellido());
        }
        if (actualizado.getNroTelefono() != null) {
            existente.setNroTelefono(actualizado.getNroTelefono());
        }
        //existente.setHabilitado(actualizado.isHabilitado());
        if (actualizado.getMail() != null) {
            existente.setMail(actualizado.getMail());
        }
        if (actualizado.getRol() != null) {
            existente.setRol(actualizado.getRol());
        }
        existente.setLatitud(actualizado.getLatitud());
        existente.setLongitud(actualizado.getLongitud());
    }

    //Obtiene un usuario si existe devuelve true
    @Transactional(readOnly = true)
    public boolean existeUsuario(Long id) {
        return repoUsuario.existsById(id);
    }

    //Habilitar a un usuario
    /*@Transactional
    public DTOUsuario habilitarUsuario(Long idUsuario) throws Exception {
        try {
            if(repoUsuario.existsById(idUsuario)) {
                Usuario usuarioExistente = repoUsuario.findById(idUsuario)
                        .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + idUsuario));
                usuarioExistente.setHabilitado(true);
                Usuario usuarioGuardado = repoUsuario.save(usuarioExistente);
                return this.toDTO(usuarioGuardado);
            } else {
                throw new Exception("El usuario con la id: " + idUsuario + " no existe");
            }
        } catch (Exception e) {
            throw new Exception("No se pudo habilitar al usuario: " + e.getMessage());
        }
    }*/

    //Deshabilitar a un usuario
    /*@Transactional
    public DTOUsuario deshabilitarUsuario(Long idUsuario) throws Exception{
        try {
            if(repoUsuario.existsById(idUsuario)) {
                Usuario usuarioExistente = repoUsuario.findById(idUsuario)
                        .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + idUsuario));
                usuarioExistente.setHabilitado(false);
                Usuario usuarioGuardado = repoUsuario.save(usuarioExistente);
                return this.toDTO(usuarioGuardado);
            } else {
                throw new Exception("El usuario con la id: " + idUsuario + " no existe");
            }
        } catch (Exception e) {
            throw new Exception("No se pudo deshabilitar al usuario: " + e.getMessage());
        }
    }*/


}
