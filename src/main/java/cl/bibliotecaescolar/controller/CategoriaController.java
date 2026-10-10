package cl.bibliotecaescolar.controller;

import cl.bibliotecaescolar.dao.CategoriaDAO;
import cl.bibliotecaescolar.dao.impl.CategoriaDAOImpl;
import cl.bibliotecaescolar.model.Categoria;
import cl.bibliotecaescolar.util.ValidadorFormulario;

import java.util.List;

/**
 *
 * Controlador para la gestión de categorías de libros en el sistema.
 */
public class CategoriaController {
    private final CategoriaDAO categoriaDAO;

    /**
     *
     * Constructor por defecto que inicializa la implementación del DAO.
     */
    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    /**
     *
     * Registra una nueva categoría previa comprobación de campos.
     *
     * @param nombre Nombre de la categoría.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String registrarCategoria(String nombre) {
        if (ValidadorFormulario.hayCamposVacios(nombre)) {
            return "El nombre de la categoría es obligatorio.";
        }

        Categoria nuevaCategoria = new Categoria(nombre.trim());
        return categoriaDAO.guardar(nuevaCategoria) ? "Categoría registrada exitosamente." : "Error al registrar la categoría en la base de datos.";
    }

    /**
     *
     * Actualiza el nombre de una categoría existente.
     *
     * @param id     ID de la categoría a modificar.
     * @param nombre Nuevo nombre para la categoría.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String actualizarCategoria(int id, String nombre) {
        if (id <= 0) {
            return "ID de categoría no válido.";
        }

        if (ValidadorFormulario.hayCamposVacios(nombre)) {
            return "El nombre de la categoría es obligatorio.";
        }

        Categoria categoria = new Categoria(id, nombre.trim());
        return categoriaDAO.actualizar(categoria) ? "Categoría actualizada correctamente." : "No se pudo actualizar la categoría.";
    }

    /**
     *
     * Elimina una categoría por su ID.
     *
     * @param id ID de la categoría a eliminar.
     * @return Mensaje descriptivo del resultado.
     */
    public String eliminarCategoria(int id) {
        if (id <= 0) {
            return "Seleccione una categoría válida para eliminar.";
        }

        if (categoriaDAO.eliminar(id)) {
            return "Categoría eliminada exitosamente.";
        } else {
            return "Error al eliminar la categoría. Verifique si existen libros asociados a ella.";
        }
    }

    /**
     *
     * Obtiene la lista completa de categorías registradas.
     *
     * @return Lista de objetos Categoria.
     */
    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarTodas();
    }

    /**
     *
     * Busca una categoría por su identificador único.
     *
     * @param id ID de la categoría.
     * @return Objeto Categoria o null si no existe.
     */
    public Categoria obtenerPorId(int id) {
        if (id <= 0) return null;
        return categoriaDAO.obtenerPorId(id);
    }
}
