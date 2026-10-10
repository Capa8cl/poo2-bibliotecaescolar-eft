package cl.bibliotecaescolar.model;

/**
 *
 * Representa una plantilla genérica (clase abstracta) para la Persona de la Biblioteca Escolar.
 */
public abstract class Persona {
    private int id;
    private String nombre;
    private String rut;
    private String email;

    /**
     *
     * Constructor de la Persona cuando no se ingresa un ID manualmente.
     *
     * @param nombre Nombre de la Persona.
     * @param rut    RUT de la Persona.
     * @param email  Correo electrónico de la Persona.
     */
    public Persona(String nombre, String rut, String email) {
        this.nombre = nombre;
        this.rut = rut;
        this.email = email;
    }

    /**
     *
     * Constructor de la Persona cuando se ingresa un ID manualmente.
     *
     * @param id     Identificador único de la persona.
     * @param nombre Nombre de la Persona.
     * @param rut    RUT de la Persona.
     * @param email  Correo electrónico de la Persona.
     */
    public Persona(int id, String nombre, String rut, String email) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.email = email;
    }

    /**
     *
     * Método abstracto que establece el tipo de perfil.
     *
     * @return Tipo de perfil.
     */
    public abstract String getTipoPerfil();

    // Getters y Setters

    /**
     *
     * Obtiene el ID de la Persona.
     *
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * Estable el ID de la Persona.
     *
     * @param id Establece el ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * Obtiene el nombre de la Persona.
     *
     * @return Nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * Establece el nombre de la Persona.
     *
     * @param nombre Establece el nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * Obtiene el RUT de la Persona.
     *
     * @return RUT.
     */
    public String getRut() {
        return rut;
    }

    /**
     *
     * Establece el RUT de la Persona.
     *
     * @param rut Establece RUT.
     */
    public void setRut(String rut) {
        this.rut = rut;
    }

    /**
     *
     * Obtiene el Email de la Persona.
     *
     * @return Email.
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * Establece el Email de la Persona.
     *
     * @param email Establece Email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * Devuelve una representación en texto de la clase con todos sus atributos.
     *
     * @return Cadena de texto con la información detallada del objeto.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", RUT: " + rut + ", Email: " + email;
    }
}
