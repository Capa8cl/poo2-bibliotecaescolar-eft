package cl.bibliotecaescolar.model;

/**
 *
 * Representa la clase para Usuario de la Biblioteca Escolar. Extiende de Persona.
 */
public class Usuario extends Persona {
    private String contrasenia;
    private RolUsuario rol;

    /**
     *
     * Constructor del Usuario cuando no se ingresa un ID manualmente.
     *
     * @param nombre      Nombre de la Persona.
     * @param rut         RUT de la Persona.
     * @param email       Correo electrónico de la Persona.
     * @param contrasenia Contraseña del Usuario.
     * @param rol         Rol asignado al usuario (Enum Rol: ESTUDIANTE, BIBLIOTECARIO).
     */
    public Usuario(String nombre, String rut, String email, String contrasenia, RolUsuario rol) {
        super(nombre, rut, email);
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    /**
     *
     * Constructor del Usuario cuando se ingresa un ID manualmente.
     *
     * @param id          Identificador único del Usuario.
     * @param nombre      Nombre de la Persona.
     * @param rut         RUT de la Persona.
     * @param email       Correo electrónico de la Persona.
     * @param contrasenia Contraseña del Usuario.
     * @param rol         Rol asignado al usuario (Enum Rol: ESTUDIANTE, BIBLIOTECARIO).
     */
    public Usuario(int id, String nombre, String rut, String email, String contrasenia, RolUsuario rol) {
        super(id, nombre, rut, email);
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    /**
     *
     * Devuelve una representación en texto del tipo de perfil y rol del usuario.
     *
     * @return String con el tipo de perfil y rol.
     */
    @Override
    public String getTipoPerfil() {
        return "Usuario - Rol: " + this.rol;
    }

    // Getters y Setters

    /**
     *
     * Obtiene la contraseña del Usuario.
     *
     * @return Contraseña.
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     *
     * Establece la contraseña del Usuario.
     *
     * @param contrasenia Establece la contraseña.
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     *
     * Obtiene el Rol del Usuario.
     *
     * @return Rol.
     */
    public RolUsuario getRol() {
        return rol;
    }

    /**
     *
     * Establece el Rol del Usuario.
     *
     * @param rol Establece el Rol.
     */
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    /**
     *
     * Devuelve una representación en texto de la clase con todos sus atributos menos la contraseña por medida de seguridad.
     *
     * @return Cadena de texto con la información detallada del objeto (sin la contraseña).
     */
    @Override
    public String toString() {
        return super.toString() + ", Rol: " + this.rol;
    }
}
