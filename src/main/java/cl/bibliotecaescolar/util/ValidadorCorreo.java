package cl.bibliotecaescolar.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Clase de utilidad para validar el formato de correo electrónico.
 */
public class ValidadorCorreo {
    private static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern PATTERN = Pattern.compile(REGEX_CORREO);

    /**
     *
     * Valida si el correo tiene un formato correcto.
     *
     * @param correo Cadena de texto a evaluar.
     * @return true si cumple con el formato estándar, false en caso contrario.
     */
    public static boolean esValido(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = PATTERN.matcher(correo.trim());
        return matcher.matches();
    }

    /**
     *
     * Elimina espacios en blanco innecesarios y convierte el correo a minúsculas.
     *
     * @param correo Correo a normalizar.
     * @return Correo limpio en minúsculas o null si el parámetro es nulo.
     */
    public static String limpiarFormato(String correo) {
        if (correo == null) return null;
        return correo.trim().toLowerCase();
    }
}
