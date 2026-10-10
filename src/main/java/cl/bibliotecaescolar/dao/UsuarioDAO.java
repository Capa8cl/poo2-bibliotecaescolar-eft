package cl.bibliotecaescolar.dao;

import cl.bibliotecaescolar.model.Usuario;

import java.util.List;

/**
 *
 * Define las operaciones de persistencia disponibles para Usuario.
 */
public interface UsuarioDAO {

    /**
     *
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto Usuario a registrar.
     * @return true si fue guardado exitosamente, false en caso contrario.
     */
    boolean guardar(Usuario usuario);

    /**
     *
     * Autentica a un usuario en el sistema verificando sus credenciales.
     *
     * @param correo      Correo electrónico del usuario.
     * @param contrasenia Contraseña del usuario.
     * @return El objeto Usuario con los datos y rol si las credenciales son válidas, o null si son incorrectas.
     */
    Usuario autenticar(String correo, String contrasenia);

    /**
     *
     * Obtiene un usuario específico de la base de datos a partir de su ID.
     *
     * @param id ID del usuario a buscar.
     * @return Objeto Usuario si se encuentra, o null si no existe.
     */
    Usuario obtenerPorId(int id);

    /**
     *
     * Obtiene la lista completa de todos los usuarios registrados.
     *
     * @return Lista de objetos Usuario.
     */
    List<Usuario> listarTodos();

    /**
     *
     * Actualiza la información de un usuario existente.
     *
     * @param usuario Objeto Usuario con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    boolean actualizar(Usuario usuario);

    /**
     *
     * Elimina un usuario de la base de datos según su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    boolean eliminar(int id);
}
