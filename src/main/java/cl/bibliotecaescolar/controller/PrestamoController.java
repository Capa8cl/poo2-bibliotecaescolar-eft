package cl.bibliotecaescolar.controller;

import cl.bibliotecaescolar.dao.EstudianteDAO;
import cl.bibliotecaescolar.dao.LibroDAO;
import cl.bibliotecaescolar.dao.PrestamoDAO;
import cl.bibliotecaescolar.dao.impl.EstudianteDAOImpl;
import cl.bibliotecaescolar.dao.impl.LibroDAOImpl;
import cl.bibliotecaescolar.dao.impl.PrestamoDAOImpl;
import cl.bibliotecaescolar.model.Bibliotecario;
import cl.bibliotecaescolar.model.Estudiante;
import cl.bibliotecaescolar.model.Libro;
import cl.bibliotecaescolar.model.Prestamo;
import cl.bibliotecaescolar.model.ProcesadorPrestamos;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * Controlador para la gestión de préstamos y devoluciones de libros.
 */
public class PrestamoController {
    private final ProcesadorPrestamos procesador;
    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;
    private final EstudianteDAO estudianteDAO;
    private static final int DIAS_PRESTAMO_ESTANDAR = 7;

    /**
     *
     * Constructor por defecto. Inicializa una nueva instancia de ProcesadorPrestamos.
     */
    public PrestamoController() {
        this.procesador = new ProcesadorPrestamos();
        this.prestamoDAO = new PrestamoDAOImpl();
        this.libroDAO = new LibroDAOImpl();
        this.estudianteDAO = new EstudianteDAOImpl();
    }

    /**
     *
     * Constructor que recibe un ProcesadorPrestamos existente (para compartir la cola con otras vistas).
     *
     * @param procesador Instancia compartida de la cola de préstamos.
     */
    public PrestamoController(ProcesadorPrestamos procesador) {
        this.procesador = (procesador != null) ? procesador : new ProcesadorPrestamos();
        this.prestamoDAO = new PrestamoDAOImpl();
        this.libroDAO = new LibroDAOImpl();
        this.estudianteDAO = new EstudianteDAOImpl();
    }

    /**
     *
     * Registra una solicitud de préstamo y la añade al procesador (cola compartida).
     *
     * @param idEstudiante ID del estudiante.
     * @param idLibro      ID del libro a prestar.
     * @param diasPrestamo Días solicitados.
     * @return Objeto Prestamo encolado o null si los IDs no son válidos.
     */
    public Prestamo encolarPrestamo(int idEstudiante, int idLibro, int diasPrestamo) {
        if (idEstudiante <= 0 || idLibro <= 0) {
            return null;
        }

        int dias = (diasPrestamo > 0) ? diasPrestamo : DIAS_PRESTAMO_ESTANDAR;
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(dias);

        Prestamo nuevoPrestamo = new Prestamo(idEstudiante, idLibro, fechaPrestamo, fechaDevolucion, false);
        this.procesador.agregarPrestamo(nuevoPrestamo);

        return nuevoPrestamo;
    }

    /**
     *
     * Inicia el procesamiento de las solicitudes pendientes en un hilo separado utilizando a un Bibliotecario.
     *
     * @param bibliotecario Instancia de Bibliotecario (Runnable) que ejecutará la atención.
     */
    public void iniciarProcesamiento(Bibliotecario bibliotecario) {
        if (bibliotecario != null && !this.procesador.estaVacio()) {
            bibliotecario.setProcesador(this.procesador);

            Thread hiloBibliotecario = new Thread(bibliotecario, "Hilo-Bibliotecario-" + bibliotecario.getNombre());
            hiloBibliotecario.start();
        }
    }

    /**
     *
     * Lógica de para registrar un préstamo.
     * Verifica stock, actualiza stock de forma sincronizada y guarda en la BD.
     *
     * @param idEstudiante ID del estudiante.
     * @param idLibro      ID del libro.
     * @param diasPrestamo Días solicitados.
     * @return Mensaje de respuesta del proceso.
     */
    public String registrarPrestamo(int idEstudiante, int idLibro, int diasPrestamo) {
        if (idEstudiante <= 0 || idLibro <= 0) {
            return "Debe seleccionar un estudiante y un libro válido.";
        }

        Libro libro = libroDAO.obtenerPorId(idLibro);
        if (libro == null) {
            return "El libro seleccionado no existe.";
        }

        if (libro.getStock() <= 0) {
            return "El libro '" + libro.getTitulo() + "' no tiene stock disponible.";
        }

        boolean stockActualizado = libroDAO.actualizarStock(idLibro, -1);
        if (!stockActualizado) {
            return "No fue posible reservar el libro (stock no disponible).";
        }

        int dias = (diasPrestamo > 0) ? diasPrestamo : DIAS_PRESTAMO_ESTANDAR;
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(dias);

        Prestamo nuevoPrestamo = new Prestamo(idEstudiante, idLibro, fechaPrestamo, fechaDevolucion, false);

        if (prestamoDAO.guardar(nuevoPrestamo)) {
            return "Préstamo registrado correctamente. Devolución: " + fechaDevolucion;
        } else {
            libroDAO.actualizarStock(idLibro, 1);
            return "Error al guardar el registro del préstamo.";
        }
    }

    /**
     *
     * Registra la devolución de un libro, cambiando su estado a devuelto y reincorporando el stock.
     *
     * @param idPrestamo ID del préstamo a devolver.
     * @return Mensaje descriptivo con el resultado de la operación.
     */
    public String registrarDevolucion(int idPrestamo) {
        if (idPrestamo <= 0) {
            return "Seleccione un préstamo válido.";
        }

        Prestamo prestamo = prestamoDAO.obtenerPorId(idPrestamo);
        if (prestamo == null) {
            return "El préstamo solicitado no existe.";
        }

        if (prestamo.isDevuelto()) {
            return "Este préstamo ya fue marcado como devuelto previamente.";
        }

        prestamo.setDevuelto(true);
        if (prestamoDAO.actualizar(prestamo)) {
            libroDAO.actualizarStock(prestamo.getIdLibro(), 1);
            return "Devolución registrada correctamente. Stock del libro actualizado.";
        } else {
            return "Error al intentar actualizar el estado del préstamo.";
        }
    }

    /**
     *
     * Obtiene la lista de todos los préstamos registrados.
     *
     * @return Lista de objetos Prestamo.
     */
    public List<Prestamo> listarTodos() {
        return prestamoDAO.listarTodos();
    }

    /**
     *
     * Obtiene únicamente la lista de préstamos activos (no devueltos).
     *
     * @return Lista de objetos Prestamo vigentes.
     */
    public List<Prestamo> listarPrestamosActivos() {
        return prestamoDAO.listarActivos();
    }

    /**
     *
     * Obtiene el historial de préstamos de un estudiante en particular.
     *
     * @param idEstudiante ID del estudiante.
     * @return Lista de objetos Prestamo asociados al estudiante.
     */
    public List<Prestamo> listarPorEstudiante(int idEstudiante) {
        if (idEstudiante <= 0) return List.of();
        return prestamoDAO.listarPorEstudiante(idEstudiante);
    }

    /**
     *
     * Busca un préstamo por su ID.
     *
     * @param id ID del préstamo.
     * @return Objeto Prestamo o null si no se encuentra.
     */
    public Prestamo obtenerPorId(int id) {
        if (id <= 0) return null;
        return prestamoDAO.obtenerPorId(id);
    }

    /**
     *
     * Obtiene la instancia del procesador/cola de préstamos.
     *
     * @return Objeto ProcesadorPrestamos.
     */
    public ProcesadorPrestamos getProcesador() {
        return procesador;
    }

    /**
     *
     * Obtiene únicamente los libros que cuentan con stock disponible (> 0).
     *
     * @return Lista de objetos Libro disponibles para préstamo.
     */
    public List<Libro> obtenerLibrosDisponibles() {
        return libroDAO.listarTodos().stream().filter(libro -> libro.getStock() > 0).toList();
    }

    /**
     *
     * Obtiene la lista completa de estudiantes.
     *
     * @return Lista de objetos Estudiante.
     */
    public List<Estudiante> obtenerEstudiantes() {
        return estudianteDAO.listarTodos();
    }
}