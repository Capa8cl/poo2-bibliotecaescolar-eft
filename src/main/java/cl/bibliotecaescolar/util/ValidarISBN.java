package cl.bibliotecaescolar.util;

/**
 *
 * Clase de utilidad para validar el ISBN.
 */
public class ValidarISBN {

    /**
     *
     * Valida si el texto ingresado corresponde a un formato numérico de ISBN-10 o ISBN-13.
     *
     * @param isbn Cadena de texto con el ISBN a evaluar.
     * @return Devuelve true si cumple con el formato de 10 o 13 dígitos, false en caso contrario.
     */
    public static boolean esIsbnNumerico(String isbn) {
        String limpio = isbn.replaceAll("[- ]", "");

        boolean esIsbn13 = limpio.matches("^[0-9]{13}$");

        boolean esIsbn10 = limpio.matches("^[0-9]{9}[0-9Xx]$");

        return esIsbn13 || esIsbn10;
    }
}