package cl.bibliotecaescolar.dao;

import cl.bibliotecaescolar.model.Categoria;

import java.util.List;

/**
 *
 * Define las operaciones de persistencia disponibles para Categoría.
 */
public interface CategoriaDAO {

    /**
     *
     * Guarda una nueva categoría en la base de datos.
     *
     * @param categoria Objeto Categoría a registrar.
     * @return Devuelve true si fue guardada y false si no fue guardada.
     */
    boolean guardar(Categoria categoria);

    /**
     *
     * Obtiene una categoría específica de la base de datos a partir de su ID.
     *
     * @param id ID de la categoría a buscar.
     * @return Objeto Categoría si se encuentra, o null si no existe.
     */
    Categoria obtenerPorId(int id);

    /**
     *
     * Obtiene la lista completa de todas las categorías registradas en la base de datos.
     *
     * @return Lista de objetos Categoría. Si no hay registros, retorna una lista vacía.
     */
    List<Categoria> listarTodas();

    /**
     *
     * Actualiza la información de una categoría existente en la base de datos.
     *
     * @param categoria Objeto Categoría con los datos actualizados.
     * @return Devuelve true si la categoría fue actualizada o false en caso contrario.
     */
    boolean actualizar(Categoria categoria);

    /**
     *
     * Elimina una categoría de la base de datos según su ID.
     *
     * @param id Identificador único de la categoría a eliminar.
     * @return Devuelve true si la categoría fue eliminada o false en caso contrario.
     */
    boolean eliminar(int id);
}
