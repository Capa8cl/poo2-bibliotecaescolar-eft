package cl.bibliotecaescolar.controller;

import cl.bibliotecaescolar.dao.UsuarioDAO;
import cl.bibliotecaescolar.dao.impl.UsuarioDAOImpl;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.model.RolUsuario;
import cl.bibliotecaescolar.util.ValidadorCorreo;
import cl.bibliotecaescolar.util.ValidadorFormulario;
import cl.bibliotecaescolar.util.ValidadorRut;

import java.util.List;

/**
 *
 * Controlador para la gestión de usuarios e inicio de sesión en el sistema.
 */
public class UsuarioController {
    private final UsuarioDAO usuarioDAO;

    /**
     *
     * Constructor por defecto que inicializa la implementación del DAO.
     */
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     *
     * Autentica a un usuario comprobando su correo y contraseña.
     *
     * @param correo      Correo electrónico ingresado.
     * @param contrasenia Contraseña ingresada.
     * @return El objeto Usuario si las credenciales son válidas, o null si son incorrectas o faltan datos.
     */
    public Usuario iniciarSesion(String correo, String contrasenia) {
        if (ValidadorFormulario.hayCamposVacios(correo, contrasenia) || !ValidadorCorreo.esValido(correo)) {
            return null;
        }

        String correoLimpio = ValidadorCorreo.limpiarFormato(correo);
        return usuarioDAO.autenticar(correoLimpio, contrasenia);
    }

    /**
     *
     * Registra un nuevo usuario previa validación de los datos.
     *
     * @param nombre      Nombre completo.
     * @param rut         RUT en cualquier formato válido.
     * @param correo      Correo electrónico.
     * @param contrasenia Contraseña.
     * @param rol         Rol asignado (RolUsuario).
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String registrarUsuario(String nombre, String rut, String correo, String contrasenia, RolUsuario rol) {
        String error = ValidadorFormulario.validarUsuario(nombre, rut, correo, contrasenia, rol);
        if (error != null) {
            return error;
        }

        String rutLimpio = ValidadorRut.limpiarFormato(rut);
        String correoLimpio = ValidadorCorreo.limpiarFormato(correo);

        Usuario nuevoUsuario = new Usuario(nombre.trim(), rutLimpio, correoLimpio, contrasenia, rol);
        return usuarioDAO.guardar(nuevoUsuario) ? "Usuario registrado exitosamente." : "Error al registrar el usuario. Verifique si el RUT o correo ya existen en el sistema.";
    }

    /**
     *
     * Actualiza la información de un usuario existente previa validación.
     *
     * @param id          ID del usuario a modificar.
     * @param nombre      Nuevo nombre.
     * @param rut         Nuevo RUT.
     * @param correo      Nuevo correo.
     * @param contrasenia Nueva contraseña.
     * @param rol         Nuevo rol.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String actualizarUsuario(int id, String nombre, String rut, String correo, String contrasenia, RolUsuario rol) {
        if (id <= 0) {
            return "ID de usuario no válido.";
        }

        String error = ValidadorFormulario.validarUsuario(nombre, rut, correo, contrasenia, rol);
        if (error != null) {
            return error;
        }

        String rutLimpio = ValidadorRut.limpiarFormato(rut);
        String correoLimpio = ValidadorCorreo.limpiarFormato(correo);

        Usuario usuario = new Usuario(id, nombre.trim(), rutLimpio, correoLimpio, contrasenia, rol);

        if (usuarioDAO.actualizar(usuario)) {
            return "Usuario actualizado correctamente.";
        } else {
            return "No se pudo actualizar la información del usuario.";
        }
    }

    /**
     *
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Mensaje descriptivo del resultado.
     */
    public String eliminarUsuario(int id) {
        if (id <= 0) {
            return "Seleccione un usuario válido para eliminar.";
        }

        if (usuarioDAO.eliminar(id)) {
            return "Usuario eliminado exitosamente.";
        } else {
            return "Error al intentar eliminar el usuario.";
        }
    }

    /**
     *
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return Lista de objetos Usuario.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    /**
     *
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Objeto Usuario o null si no se encuentra.
     */
    public Usuario obtenerPorId(int id) {
        if (id <= 0) return null;
        return usuarioDAO.obtenerPorId(id);
    }
}
