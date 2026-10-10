package cl.bibliotecaescolar.model;

/**
 *
 * Representa la clase para Estudiante de la Biblioteca Escolar. Extiende de Persona.
 */
public class Estudiante extends Persona {
    private String curso;

    /**
     *
     * Constructor del Estudiante cuando no se ingresa un ID manualmente.
     *
     * @param nombre Nombre de la Persona.
     * @param rut    RUT de la Persona.
     * @param email  Correo electrónico de la Persona.
     * @param curso  Curso escolar correspondiente al Estudiante.
     */
    public Estudiante(String nombre, String rut, String email, String curso) {
        super(nombre, rut, email);
        this.curso = curso;
    }

    /**
     *
     * Constructor del Estudiante cuando se ingresa un ID manualmente.
     *
     * @param id     Identificador único del Estudiante.
     * @param nombre Nombre de la Persona.
     * @param rut    RUT de la Persona.
     * @param email  Correo electrónico de la Persona.
     * @param curso  Curso escolar correspondiente al Estudiante.
     */
    public Estudiante(int id, String nombre, String rut, String email, String curso) {
        super(id, nombre, rut, email);
        this.curso = curso;
    }

    /**
     *
     * Devuelve una representación en texto del tipo de perfil y curso.
     *
     * @return String con el tipo de perfil y curso.
     */
    @Override
    public String getTipoPerfil() {
        return "Estudiante - Curso: " + this.curso;
    }

    // Getters y Setters

    /**
     *
     * Obtiene el curso del Estudiante.
     *
     * @return Curso.
     */
    public String getCurso() {
        return curso;
    }

    /**
     *
     * Establece el curso del Estudiante.
     *
     * @param curso Establece el curso.
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     *
     * Devuelve una representación en texto de la clase con todos sus atributos.
     *
     * @return Cadena de texto con la información detallada del objeto.
     */
    @Override
    public String toString() {
        return "ID: " + super.getId() + ", " + super.getNombre();
    }
}
