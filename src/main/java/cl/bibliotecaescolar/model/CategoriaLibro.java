package cl.bibliotecaescolar.model;

/**
 * Enum que define las categorías de libros disponibles en la biblioteca.
 */
public enum CategoriaLibro {
    LITERATURA(1, "Literatura"), CIENCIAS(2, "Ciencias"), HISTORIA(3, "Historia"), MATEMATICAS(4, "Matemáticas"), TECNOLOGIA(5, "Tecnología");

    private final int id;
    private final String nombre;

    CategoriaLibro(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Busca una categoría por su ID numérico (útil para mapear desde la BD/ResultSet).
     */
    public static CategoriaLibro obtenerPorId(int id) {
        for (CategoriaLibro cat : values()) {
            if (cat.getId() == id) {
                return cat;
            }
        }
        return LITERATURA;
    }

    /**
     * Define lo que se mostrará en el JComboBox de Swing.
     */
    @Override
    public String toString() {
        return nombre;
    }
}