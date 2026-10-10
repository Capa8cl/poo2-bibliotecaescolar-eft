package cl.bibliotecaescolar.dao;

import cl.bibliotecaescolar.model.Estudiante;

import java.util.List;

/**
 *
 * Define las operaciones de persistencia disponibles para Estudiante.
 */
public interface EstudianteDAO {

    /**
     *
     * Guarda un nuevo estudiante en la base de datos.
     *
     * @param estudiante Objeto Estudiante a registrar.
     * @return true si fue guardado exitosamente, false en caso contrario.
     */
    boolean guardar(Estudiante estudiante);

    /**
     *
     * Obtiene un estudiante específico de la base de datos a partir de su ID.
     *
     * @param id ID del estudiante a buscar.
     * @return Objeto Estudiante si se encuentra, o null si no existe.
     */
    Estudiante obtenerPorId(int id);

    /**
     *
     * Busca un estudiante en la base de datos a través de su RUT.
     *
     * @param rut RUT del estudiante a buscar.
     * @return Objeto Estudiante si se encuentra, o null si no existe.
     */
    Estudiante obtenerPorRut(String rut);

    /**
     *
     * Obtiene la lista completa de todos los estudiantes registrados.
     *
     * @return Lista de objetos Estudiante.
     */
    List<Estudiante> listarTodos();

    /**
     * Busca un estudiante en la base de datos por su correo electrónico.
     *
     * @param correo Correo electrónico a consultar.
     * @return Objeto Estudiante si coincide, o null si no existe.
     */
    Estudiante obtenerPorCorreo(String correo);

    /**
     *
     * Actualiza la información de un estudiante existente.
     *
     * @param estudiante Objeto Estudiante con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    boolean actualizar(Estudiante estudiante);

    /**
     *
     * Elimina un estudiante de la base de datos según su ID.
     *
     * @param id ID del estudiante a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    boolean eliminar(int id);
}
