package cl.bibliotecaescolar.controller;

import cl.bibliotecaescolar.dao.EstudianteDAO;
import cl.bibliotecaescolar.dao.UsuarioDAO;
import cl.bibliotecaescolar.dao.impl.EstudianteDAOImpl;
import cl.bibliotecaescolar.dao.impl.UsuarioDAOImpl;
import cl.bibliotecaescolar.model.Estudiante;
import cl.bibliotecaescolar.model.RolUsuario;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.util.ValidadorCorreo;
import cl.bibliotecaescolar.util.ValidadorFormulario;
import cl.bibliotecaescolar.util.ValidadorRut;

import java.util.List;

/**
 *
 * Controlador para la gestión de estudiantes y su vinculación como usuarios del sistema.
 */
public class EstudianteController {
    private final EstudianteDAO estudianteDAO;
    private final UsuarioDAO usuarioDAO;

    /**
     *
     * Constructor por defecto que inicializa las implementaciones DAO.
     */
    public EstudianteController() {
        this.estudianteDAO = new EstudianteDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     *
     * Registra un nuevo estudiante y crea su cuenta de Usuario con rol 'estudiante' y la clave inicial por defecto.
     *
     * @param nombre Nombre completo del estudiante.
     * @param rut    RUT en cualquier formato válido.
     * @param correo Correo electrónico.
     * @param curso  Curso o nivel al que pertenece.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String registrarEstudiante(String nombre, String rut, String correo, String curso) {
        if (ValidadorFormulario.hayCamposVacios(nombre, rut, correo, curso)) {
            return "Todos los campos son obligatorios.";
        }

        String errorPersona = ValidadorFormulario.validarPersona(rut, correo);
        if (errorPersona != null) {
            return errorPersona;
        }

        String rutLimpio = ValidadorRut.limpiarFormato(rut);
        String correoLimpio = ValidadorCorreo.limpiarFormato(correo);

        String numeroRutBase = rutLimpio.contains("-") ? rutLimpio.split("-")[0] : rutLimpio;

        boolean existeRut = estudianteDAO.listarTodos().stream().anyMatch(e -> {
            if (e.getRut() == null) return false;
            String base = e.getRut().contains("-") ? e.getRut().split("-")[0] : e.getRut();
            return base.equals(numeroRutBase);
        });

        if (existeRut) {
            return "Ya existe un estudiante registrado con el RUT ingresado.";
        }

        if (estudianteDAO.obtenerPorCorreo(correoLimpio) != null) {
            return "Ya existe un estudiante registrado con el correo electrónico ingresado.";
        }

        Estudiante nuevoEstudiante = new Estudiante(nombre.trim(), rutLimpio, correoLimpio, curso.trim());
        boolean guardadoEstudiante = estudianteDAO.guardar(nuevoEstudiante);

        if (guardadoEstudiante) {
            Usuario nuevoUsuario = new Usuario(0, nombre.trim(), rutLimpio, correoLimpio, "clave123", RolUsuario.ESTUDIANTE);

            usuarioDAO.guardar(nuevoUsuario);

            return "Estudiante registrado de manera correcta.\nContraseña por defecto: clave123";
        }

        return "Error al registrar el estudiante en la base de datos.";
    }

    /**
     *
     * Actualiza la información del estudiante y sincroniza sus datos de Usuario.
     *
     * @param id     ID del estudiante a modificar.
     * @param nombre Nuevo nombre.
     * @param rut    Nuevo RUT.
     * @param correo Nuevo correo.
     * @param curso  Nuevo curso.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String actualizarEstudiante(int id, String nombre, String rut, String correo, String curso) {
        if (id <= 0) {
            return "ID de estudiante no válido.";
        }

        if (ValidadorFormulario.hayCamposVacios(nombre, rut, correo, curso)) {
            return "Todos los campos son obligatorios.";
        }

        String errorPersona = ValidadorFormulario.validarPersona(rut, correo);
        if (errorPersona != null) {
            return errorPersona;
        }

        String rutLimpio = ValidadorRut.limpiarFormato(rut);
        String correoLimpio = ValidadorCorreo.limpiarFormato(correo);
        String numeroRutBase = rutLimpio.contains("-") ? rutLimpio.split("-")[0] : rutLimpio;

        boolean rutUsadoPorOtro = estudianteDAO.listarTodos().stream().anyMatch(e -> e.getId() != id && e.getRut() != null && (e.getRut().contains("-") ? e.getRut().split("-")[0] : e.getRut()).equals(numeroRutBase));

        if (rutUsadoPorOtro) {
            return "El RUT ingresado pertenece a otro estudiante.";
        }

        Estudiante estudianteCorreo = estudianteDAO.obtenerPorCorreo(correoLimpio);
        if (estudianteCorreo != null && estudianteCorreo.getId() != id) {
            return "El correo electrónico ya está registrado por otro estudiante.";
        }

        Estudiante estudiante = new Estudiante(id, nombre.trim(), rutLimpio, correoLimpio, curso.trim());

        if (estudianteDAO.actualizar(estudiante)) {
            Usuario usr = usuarioDAO.obtenerPorId(id);
            if (usr != null) {
                usr.setNombre(nombre.trim());
                usr.setRut(rutLimpio);
                usr.setEmail(correoLimpio);
                usuarioDAO.actualizar(usr);
            }
            return "Estudiante actualizado correctamente.";
        } else {
            return "No se pudo actualizar la información del estudiante.";
        }
    }

    /**
     *
     * Elimina un estudiante por su ID y su cuenta de usuario.
     *
     * @param id ID del estudiante a eliminar.
     * @return Mensaje descriptivo del resultado.
     */
    public String eliminarEstudiante(int id) {
        if (id <= 0) {
            return "Seleccione un estudiante válido para eliminar.";
        }

        if (estudianteDAO.eliminar(id)) {
            usuarioDAO.eliminar(id);
            return "Estudiante eliminado correctamente.";
        } else {
            return "Error al intentar eliminar el estudiante. Verifique si tiene préstamos asociados.";
        }
    }

    /**
     *
     * Obtiene la lista completa de estudiantes registrados.
     *
     * @return Lista de objetos Estudiante.
     */
    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.listarTodos();
    }

    /**
     *
     * Busca un estudiante específico por su ID.
     *
     * @param id ID del estudiante.
     * @return Objeto Estudiante o null si no se encuentra.
     */
    public Estudiante obtenerPorId(int id) {
        if (id <= 0) return null;
        return estudianteDAO.obtenerPorId(id);
    }

    /**
     *
     * Busca un estudiante en el sistema utilizando su RUT.
     *
     * @param rut RUT a consultar.
     * @return Objeto Estudiante o null si no existe.
     */
    public Estudiante obtenerPorRut(String rut) {
        if (rut == null || rut.trim().isEmpty() || !ValidadorRut.esValido(rut)) {
            return null;
        }
        String rutLimpio = ValidadorRut.limpiarFormato(rut);
        return estudianteDAO.obtenerPorRut(rutLimpio);
    }
}