package cl.bibliotecaescolar.controller;

import cl.bibliotecaescolar.dao.LibroDAO;
import cl.bibliotecaescolar.dao.impl.LibroDAOImpl;
import cl.bibliotecaescolar.model.Libro;
import cl.bibliotecaescolar.util.ValidadorFormulario;
import cl.bibliotecaescolar.util.ValidarISBN;

import java.util.List;

/**
 *
 * Controlador para la gestión del catálogo de libros en el sistema.
 */
public class LibroController {
    private final LibroDAO libroDAO;

    /**
     *
     * Constructor por defecto que inicializa la implementación del DAO.
     */
    public LibroController() {
        this.libroDAO = new LibroDAOImpl();
    }

    /**
     *
     * Registra un nuevo libro previa validación de campos obligatorios, formato e ISBN duplicado.
     *
     * @param titulo      Título del libro.
     * @param autor       Autor del libro.
     * @param isbn        ISBN del libro.
     * @param editorial   Editorial del libro.
     * @param stock       Cantidad de ejemplares disponibles (debe ser >= 0).
     * @param idCategoria ID de la categoría asociada (debe ser > 0).
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String registrarLibro(String titulo, String autor, String isbn, String editorial, int stock, int idCategoria) {
        if (ValidadorFormulario.hayCamposVacios(titulo, autor, isbn, editorial)) {
            return "Todos los campos de texto son obligatorios.";
        }

        if (!ValidarISBN.esIsbnNumerico(isbn)) {
            return "El formato del ISBN no es válido. Debe contener 10 o 13 dígitos numéricos.";
        }

        String isbnLimpio = isbn.replaceAll("[- ]", "").trim();

        boolean existeIsbn = libroDAO.listarTodos().stream().anyMatch(l -> l.getIsbn() != null && l.getIsbn().replace("-", "").trim().equalsIgnoreCase(isbnLimpio));

        if (existeIsbn) {
            return "Ya existe un libro registrado con el ISBN ingresado.";
        }

        if (stock < 0) {
            return "El stock inicial no puede ser negativo.";
        }

        if (idCategoria <= 0) {
            return "Debe seleccionar una categoría válida.";
        }

        Libro nuevoLibro = new Libro(titulo.trim(), autor.trim(), isbnLimpio, editorial.trim(), stock, idCategoria);
        return libroDAO.guardar(nuevoLibro) ? "Libro registrado exitosamente." : "Error al registrar el libro en la base de datos.";
    }

    /**
     *
     * Actualiza la información de un libro existente.
     *
     * @param id          ID del libro a modificar.
     * @param titulo      Nuevo título.
     * @param autor       Nuevo autor.
     * @param isbn        Nuevo ISBN.
     * @param editorial   Nueva editorial.
     * @param stock       Nuevo stock.
     * @param idCategoria Nueva categoría.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String actualizarLibro(int id, String titulo, String autor, String isbn, String editorial, int stock, int idCategoria) {
        if (id <= 0) {
            return "ID de libro no válido.";
        }

        if (ValidadorFormulario.hayCamposVacios(titulo, autor, isbn, editorial)) {
            return "Todos los campos de texto son obligatorios.";
        }

        if (!ValidarISBN.esIsbnNumerico(isbn)) {
            return "El formato del ISBN no es válido.";
        }

        if (stock < 0) {
            return "El stock no puede ser un número negativo.";
        }

        if (idCategoria <= 0) {
            return "Debe seleccionar una categoría válida.";
        }

        String isbnLimpio = isbn.replaceAll("[- ]", "").trim();

        Libro libro = new Libro(id, titulo.trim(), autor.trim(), isbnLimpio, editorial.trim(), stock, idCategoria);
        return libroDAO.actualizar(libro) ? "Libro actualizado correctamente." : "No se pudo actualizar la información del libro.";
    }

    /**
     *
     * Modifica el stock de un libro de forma segura.
     *
     * @param idLibro  ID del libro a modificar.
     * @param cantidad Cantidad a sumar (positiva) o restar (negativa).
     * @return Mensaje descriptivo del resultado.
     */
    public String ajustarStock(int idLibro, int cantidad) {
        if (idLibro <= 0) {
            return "Seleccione un libro válido.";
        }

        return libroDAO.actualizarStock(idLibro, cantidad) ? "Stock actualizado correctamente." : "No se pudo actualizar el stock (verifique si hay suficiente inventario disponible).";
    }

    /**
     *
     * Elimina un libro del sistema por su ID.
     *
     * @param id ID del libro a eliminar.
     * @return Mensaje descriptivo del resultado.
     */
    public String eliminarLibro(int id) {
        if (id <= 0) {
            return "Seleccione un libro válido para eliminar.";
        }

        if (libroDAO.eliminar(id)) {
            return "Libro eliminado correctamente.";
        } else {
            return "Error al eliminar el libro. Verifique si tiene préstamos activos asociados.";
        }
    }

    /**
     *
     * Obtiene la lista completa de libros registrados.
     *
     * @return Lista de objetos Libro.
     */
    public List<Libro> listarLibros() {
        return libroDAO.listarTodos();
    }

    /**
     *
     * Obtiene la lista de libros asociados a una categoría específica.
     *
     * @param idCategoria ID de la categoría a consultar.
     * @return Lista de objetos Libro.
     */
    public List<Libro> listarPorCategoria(int idCategoria) {
        if (idCategoria <= 0) return List.of();
        return libroDAO.listarPorCategoria(idCategoria);
    }

    /**
     *
     * Busca un libro por su ID.
     *
     * @param id ID del libro.
     * @return Objeto Libro o null si no existe.
     */
    public Libro obtenerPorId(int id) {
        if (id <= 0) return null;
        return libroDAO.obtenerPorId(id);
    }
}