package cl.bibliotecaescolar.util;

/**
 *
 * Clase de utilidad para validar la estructura del RUT.
 * No valida módulo 11 para no tener problemas con el poblamiento entregado en la base de datos.
 */
public class ValidadorRut {
    private static final String REGEX_RUT = "^(\\d{1,2}\\.?\\d{3}\\.?\\d{3}-[\\dkK]|\\d{7,8}-[\\dkK])$";

    /**
     *
     * Valida que el texto ingresado cumpla con el formato estándar de RUT chileno.
     *
     * @param rut Cadena de texto a evaluar.
     * @return true si tiene un formato válido o false en caso contrario.
     */
    public static boolean esValido(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }
        return rut.trim().matches(REGEX_RUT);
    }

    /**
     *
     * Limpia los puntos del RUT y asegura que la letra K final quede en mayúscula.
     *
     * @param rut RUT a formatear.
     * @return RUT limpio listo para guardar en la base de datos.
     */
    public static String limpiarFormato(String rut) {
        if (rut == null) return null;
        return rut.replace(".", "").trim().toUpperCase();
    }
}
