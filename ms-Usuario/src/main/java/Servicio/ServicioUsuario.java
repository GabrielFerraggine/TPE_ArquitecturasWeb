package Servicio;

import Entidades.Usuario;
import Modelos.Monopatin;
import Repository.RepositoryUsuario;
import DTO.DTOUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioUsuario {

    @Autowired
    private RepositoryUsuario repoUsuario;

    public DTOUsuario toDTO(Usuario u) {
        DTOUsuario dtoUsuario = new DTOUsuario(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
                u.getCuentas(),
                u.getMail(),
                u.getRol(),
                u.getLatitud(),
                u.getLongitud(),
                u.getMonopatines(),
                u.getViajes()
        );
        return dtoUsuario;
    }

    /*g. Como usuario quiero un listado de los monopatines cercanos a mi zona, para poder encontrar
    un monopatín cerca de mi ubicación*/
    //Devuelve los 5 monopatines mas cercanos
    /*@Transactional(readOnly = true)
    public List<Monopatin> obtenerMonopatinesCercanos(Long idUsuario) throws Exception{
        try {
            DTOUsuario usuario = obtenerUsuario(idUsuario);
            //tengo que hacer algun calculo para que devuelva los 5 mas cercanos
            //quizas usar algun metodo extra que calcule las distancias y ordene, luego en esta funcion guardo los mas cercanos
            return null;
        } catch (Exception e) {
            throw new Exception("No se pudo retornar los monopatines mas cercanos");
        }
    }*/
    //se consume desde monopatin

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
    @Transactional(readOnly = true)
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
    }

    //Obtener todos los usuarios deshabilitados
    @Transactional(readOnly = true)
    public List<DTOUsuario> obtenerUsuariosDehabilitados() throws Exception {
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
    }

    //Obtener un usuario
    @Transactional(readOnly = true)
    public DTOUsuario obtenerUsuario(Long id) throws Exception{
        try {
            Usuario usuario = repoUsuario.obtenerUsuario(id);
            DTOUsuario dtoUsuario = this.toDTO(usuario);
            return dtoUsuario;
        } catch (Exception e) {
            throw new Exception("Error al obtener estudiante con la id: " + e.getMessage());
        }
    }

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
    public boolean eliminarUsuario(Long id) throws Exception{
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


    //Modificar un usuario
    @Transactional
    public DTOUsuario actualizarUsuario(Long id, Usuario usuario) throws Exception {
        try {
            if (repoUsuario.existsById(id)){
                Usuario usuarioGuardado = repoUsuario.save(usuario);
                DTOUsuario dtoUsuario = this.toDTO(usuarioGuardado);
                return dtoUsuario;
            }
            throw new Exception("Usuario no encontrado");
        } catch (Exception e) {
            throw new Exception("Error al actualizar el usuario con id=" + id + " " + e.getMessage());
        }
    }












}
