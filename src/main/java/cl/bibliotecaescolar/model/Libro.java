package cl.bibliotecaescolar.model;

/**
 *
 * Representa la clase para el Libro de la Biblioteca Escolar.
 */
public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private int stock;
    private int idCategoria;

    /**
     *
     * Constructor del Libro cuando no se ingresa un ID manualmente.
     *
     * @param titulo      Título del Libro.
     * @param autor       Autor del Libro.
     * @param isbn        ISBN del Libro.
     * @param editorial   Editorial del Libro.
     * @param stock       Stock del Libro.
     * @param idCategoria Categoría del Libro.
     */
    public Libro(String titulo, String autor, String isbn, String editorial, int stock, int idCategoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    /**
     *
     * Constructor del Libro cuando se ingresa un ID manualmente.
     *
     * @param id          Identificador único del Libro.
     * @param titulo      Título del Libro.
     * @param autor       Autor del Libro.
     * @param isbn        ISBN del Libro.
     * @param editorial   Editorial del Libro.
     * @param stock       Stock del Libro.
     * @param idCategoria Categoría del Libro.
     */
    public Libro(int id, String titulo, String autor, String isbn, String editorial, int stock, int idCategoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters

    /**
     *
     * Obtiene el ID del Libro.
     *
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * Establece el ID del Libro.
     *
     * @param id Establece ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * Obtiene el título del Libro.
     *
     * @return Título.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     *
     * Establece el título del Libro.
     *
     * @param titulo Establece título.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * Obtiene el autor del Libro.
     *
     * @return Autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     *
     * Establece el autor del Libro.
     *
     * @param autor Establece autor.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     *
     * Obtiene el ISBN del Libro.
     *
     * @return ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     *
     * Establece el ISBN del Libro.
     *
     * @param isbn Establece ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     *
     * Obtiene la editorial del Libro.
     *
     * @return Editorial.
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     *
     * Establece la editorial del Libro.
     *
     * @param editorial Establece editorial.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     *
     * Obtiene el stock del Libro.
     *
     * @return Stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * Establece el stock del Libro.
     *
     * @param stock Establece stock.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * Obtiene el ID de la categoría del Libro.
     *
     * @return ID de la categoría.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     *
     * Establece el ID de la categoría del Libro.
     *
     * @param idCategoria Establece ID de la categoría.
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     *
     * Devuelve una representación en texto de la clase con todos sus atributos.
     *
     * @return Cadena de texto con la información detallada del objeto.
     */
    @Override
    public String toString() {
        if (titulo != null && titulo.length() > 25) {
            return "ID: " + id + ", " + titulo.substring(0, 22) + "...";
        }
        return "ID: " + id + ",  " + titulo;
    }
}
