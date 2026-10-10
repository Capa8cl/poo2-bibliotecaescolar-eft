package cl.bibliotecaescolar.dao;

import cl.bibliotecaescolar.model.Libro;

import java.util.List;

/**
 *
 * Define las operaciones de persistencia disponibles para Libro.
 */
public interface LibroDAO {

    /**
     *
     * Guarda un nuevo libro en la base de datos.
     *
     * @param libro Objeto Libro a registrar.
     * @return true si fue guardado exitosamente, false en caso contrario.
     */
    boolean guardar(Libro libro);

    /**
     *
     * Obtiene un libro de la base de datos a partir de su ID.
     *
     * @param id ID del libro a buscar.
     * @return Objeto Libro si se encuentra, o null si no existe.
     */
    Libro obtenerPorId(int id);

    /**
     *
     * Obtiene la lista completa de todos los libros registrados.
     *
     * @return Lista de objetos Libro.
     */
    List<Libro> listarTodos();

    /**
     *
     * Obtiene los libros pertenecientes a una categoría específica.
     *
     * @param idCategoria ID de la categoría a filtrar.
     * @return Lista de libros de esa categoría.
     */
    List<Libro> listarPorCategoria(int idCategoria);

    /**
     *
     * Actualiza la información de un libro existente.
     *
     * @param libro Objeto Libro con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    boolean actualizar(Libro libro);

    /**
     *
     * Actualiza el stock de un libro de forma segura ante peticiones concurrentes.
     *
     * @param idLibro  ID del libro.
     * @param cantidad Cantidad a sumar (positiva) o restar (negativa).
     * @return true si el stock fue actualizado correctamente, false en caso contrario.
     */
    boolean actualizarStock(int idLibro, int cantidad);

    /**
     *
     * Elimina un libro de la base de datos según su ID.
     *
     * @param id ID del libro a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    boolean eliminar(int id);
}
