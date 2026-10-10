package cl.bibliotecaescolar.dao;

import cl.bibliotecaescolar.model.Prestamo;

import java.util.List;

/**
 *
 * Define las operaciones de persistencia disponibles para Préstamo.
 */
public interface PrestamoDAO {

    /**
     *
     * Guarda un nuevo préstamo en la base de datos.
     *
     * @param prestamo Objeto Prestamo a registrar.
     * @return true si el préstamo fue registrado o false en caso contrario.
     */
    boolean guardar(Prestamo prestamo);

    /**
     *
     * Obtiene un préstamo específico a partir de su ID.
     *
     * @param id ID del préstamo.
     * @return Objeto Prestamo si existe, null en caso contrario.
     */
    Prestamo obtenerPorId(int id);

    /**
     *
     * Obtiene la lista completa de todos los préstamos registrados en la base de datos.
     *
     * @return Lista de objetos Prestamo.
     */
    List<Prestamo> listarTodos();

    /**
     *
     * Obtiene el historial de préstamos realizados por un estudiante específico.
     *
     * @param idEstudiante ID del estudiante.
     * @return Lista de préstamos asociados al estudiante.
     */
    List<Prestamo> listarPorEstudiante(int idEstudiante);

    /**
     *
     * Obtiene la lista de préstamos que aún no han sido devueltos (devuelto = false).
     *
     * @return Lista de préstamos activos/pendientes.
     */
    List<Prestamo> listarActivos();

    /**
     *
     * Actualiza la información de un préstamo.
     *
     * @param prestamo Objeto Prestamo con los datos actualizados.
     * @return true si se actualizó o false en caso contrario.
     */
    boolean actualizar(Prestamo prestamo);

    /**
     *
     * Elimina un registro de préstamo por su ID.
     *
     * @param id ID del préstamo a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    boolean eliminar(int id);
}
