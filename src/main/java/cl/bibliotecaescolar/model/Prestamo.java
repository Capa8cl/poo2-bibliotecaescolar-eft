package cl.bibliotecaescolar.model;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private int idEstudiante;
    private int idLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    /**
     *
     * Constructor del Préstamo.
     *
     * @param idEstudiante    ID del estudiante del Préstamo.
     * @param idLibro         ID del Libro del Préstamo.
     * @param fechaPrestamo   Fecha del Préstamo.
     * @param fechaDevolucion Fecha de la devolución del libro del Préstamo.
     * @param devuelto        Estado del préstamo: true si fue devuelto, false si no fue devuelto.
     */
    public Prestamo(int idEstudiante, int idLibro, LocalDate fechaPrestamo, LocalDate fechaDevolucion, boolean devuelto) {
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    /**
     *
     * Constructor del Préstamo.
     *
     * @param id              Identificador único del Préstamo.
     * @param idEstudiante    ID del estudiante del Préstamo.
     * @param idLibro         ID del Libro del Préstamo.
     * @param fechaPrestamo   Fecha del Préstamo.
     * @param fechaDevolucion Fecha de la devolución del libro del Préstamo.
     * @param devuelto        Estado del préstamo: true si fue devuelto, false si no fue devuelto.
     */
    public Prestamo(int id, int idEstudiante, int idLibro, LocalDate fechaPrestamo, LocalDate fechaDevolucion, boolean devuelto) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    // Getters y Setters

    /**
     *
     * Obtiene el ID del Préstamo.
     *
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * Establece el ID del Préstamo.
     *
     * @param id Establece ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * Obtiene el ID del estudiante del Préstamo.
     *
     * @return ID del estudiante.
     */
    public int getIdEstudiante() {
        return idEstudiante;
    }

    /**
     *
     * Establece el ID del estudiante del Préstamo..
     *
     * @param idEstudiante Establece ID del estudiante.
     */
    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    /**
     *
     * Obtiene el ID del libro del Préstamo.
     *
     * @return ID del libro.
     */
    public int getIdLibro() {
        return idLibro;
    }

    /**
     *
     * Establece el ID del libro.
     *
     * @param idLibro Establece el ID del libro.
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    /**
     *
     * Obtiene la fecha del Préstamo.
     *
     * @return Fecha.
     */
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     *
     * Establece la fecha del Préstamo.
     *
     * @param fechaPrestamo Establece la fecha.
     */
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    /**
     *
     * Obtiene la fecha de la devolución del libro del Préstamo.
     *
     * @return Fecha de la devolución.
     */
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     *
     * Establece la fecha de la devolución del libro del Préstamo.
     *
     * @param fechaDevolucion Fecha.
     */
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     *
     * ¿El libro del Préstamo fue devuelto?
     *
     * @return Retorna true si fue devuelto, false si no fue devuelto.
     */
    public boolean isDevuelto() {
        return devuelto;
    }

    /**
     *
     * Establece si el libro del Préstamo fue devuelto.
     *
     * @param devuelto Establece true si fue devuelto, false si no fue devuelto.
     */
    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    /**
     * Devuelve una representación legible y compacta del préstamo para la interfaz gráfica.
     *
     * @return Cadena de texto simplificada del préstamo.
     */
    @Override
    public String toString() {
        return "Préstamo #" + id + ",  Libro ID: " + idLibro + ", Estudiante ID: " + idEstudiante;
    }
}
