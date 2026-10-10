package cl.bibliotecaescolar.model;

import cl.bibliotecaescolar.dao.LibroDAO;
import cl.bibliotecaescolar.dao.PrestamoDAO;
import cl.bibliotecaescolar.dao.impl.LibroDAOImpl;
import cl.bibliotecaescolar.dao.impl.PrestamoDAOImpl;

/**
 *
 * Representa la clase para el Bibliotecario encargado de procesar los préstamos de la Biblioteca Escolar.
 * Extiende de Persona e implementa Runnable.
 */
public class Bibliotecario extends Persona implements Runnable {
    private ProcesadorPrestamos procesador;

    /**
     *
     * Constructor del Bibliotecario cuando se ingresa un ID manualmente.
     *
     * @param id         ID del Bibliotecario.
     * @param nombre     Nombre del Bibliotecario.
     * @param rut        RUT del Bibliotecario.
     * @param email      Correo del Bibliotecario.
     * @param procesador Instancia del procesador de cola multihilo para préstamos.
     */
    public Bibliotecario(int id, String nombre, String rut, String email, ProcesadorPrestamos procesador) {
        super(id, nombre, rut, email);
        this.procesador = procesador;
    }

    /**
     *
     * Constructor del Bibliotecario cuando no se ingresa un ID manualmente.
     *
     * @param nombre     Nombre del Bibliotecario.
     * @param rut        RUT del Bibliotecario.
     * @param email      Correo del Bibliotecario.
     * @param procesador Instancia del procesador de cola multihilo para préstamos.
     */
    public Bibliotecario(String nombre, String rut, String email, ProcesadorPrestamos procesador) {
        super(nombre, rut, email);
        this.procesador = procesador;
    }

    /**
     *
     * Devuelve una representación en texto del tipo de perfil y curso.
     *
     * @return String con el tipo de perfil y curso.
     */
    @Override
    public String getTipoPerfil() {
        return "Bibliotecario";
    }

    public ProcesadorPrestamos getProcesador() {
        return procesador;
    }

    public void setProcesador(ProcesadorPrestamos procesador) {
        this.procesador = procesador;
    }

    /**
     *
     * Ejecuta el hilo del bibliotecario para procesar de forma concurrente la cola de préstamos.
     */
    @Override
    public void run() {
        LibroDAO libroDAO = new LibroDAOImpl();
        PrestamoDAO prestamoDAO = new PrestamoDAOImpl();

        while (procesador != null && !procesador.estaVacio()) {
            Prestamo prestamo = procesador.retirarPrestamo();
            if (prestamo == null) {
                break;
            }

            System.out.println("[Bibliotecario - " + getNombre() + "] Atendiendo solicitud para Libro ID #" + prestamo.getIdLibro());

            boolean stockActualizado = libroDAO.actualizarStock(prestamo.getIdLibro(), -1);

            if (stockActualizado) {
                boolean guardado = prestamoDAO.guardar(prestamo);

                if (guardado) {
                    System.out.println("[Bibliotecario - " + getNombre() + "] Préstamo REGISTRADO con éxito para Estudiante ID #" + prestamo.getIdEstudiante());
                } else {
                    libroDAO.actualizarStock(prestamo.getIdLibro(), 1);
                    System.err.println("[Bibliotecario - " + getNombre() + "] Error al guardar en BD. Stock restaurado.");
                }
            } else {
                System.err.println("[Bibliotecario - " + getNombre() + "] Sin stock disponible para el Libro ID #" + prestamo.getIdLibro());
            }
        }
    }
}