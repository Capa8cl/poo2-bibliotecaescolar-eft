package cl.bibliotecaescolar.model;

/**
 *
 * Representa la clase para la Categoría de la Biblioteca Escolar.
 */
public class Categoria {
    private int id;
    private String nombre;

    /**
     *
     * Constructor de la Categoría cuando no se ingresa un ID manualmente.
     *
     * @param nombre Nombre de la Categoría.
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * Constructor de la Categoría cuando se ingresa un ID manualmente.
     *
     * @param id     ID de la Categoría.
     * @param nombre Nombre de la Categoría.
     */
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters

    /**
     *
     * Obtiene el ID de la Categoría.
     *
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * Establece el ID de la Categoría.
     *
     * @param id Establece ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * Obtiene el nombre de la Categoría.
     *
     * @return Nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * Establece el nombre de la Categoría.
     *
     * @param nombre Establece el nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * Devuelve el nombre de la Categoría en formato de texto.
     *
     * @return Nombre de la Categoría.
     */
    @Override
    public String toString() {
        return "Nombre: " + nombre;
    }
}
