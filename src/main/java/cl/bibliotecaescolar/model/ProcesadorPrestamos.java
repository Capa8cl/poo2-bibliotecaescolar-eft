package cl.bibliotecaescolar.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * Recurso compartido (cola sincronizada) que almacena los préstamos pendientes de procesar.
 */
public class ProcesadorPrestamos {
    private final Queue<Prestamo> colaPrestamos = new LinkedList<>();

    /**
     *
     * Agrega una solicitud de préstamo a la cola de forma sincronizada.
     *
     * @param prestamo Préstamo a encolar.
     */
    public synchronized void agregarPrestamo(Prestamo prestamo) {
        this.colaPrestamos.add(prestamo);
    }

    /**
     *
     * Retira el siguiente préstamo disponible en la cola.
     *
     * @return Objeto Prestamo o null si está vacía.
     */
    public synchronized Prestamo retirarPrestamo() {
        return this.colaPrestamos.poll();
    }

    /**
     *
     * Verifica si la cola no contiene elementos.
     *
     * @return true si está vacía, false en caso contrario.
     */
    public synchronized boolean estaVacio() {
        return this.colaPrestamos.isEmpty();
    }

    /**
     *
     * Obtiene la cantidad de préstamos pendientes en cola.
     *
     * @return int con la cantidad de elementos.
     */
    public synchronized int cantidadPendientes() {
        return this.colaPrestamos.size();
    }

}
