package cl.bibliotecaescolar.util;

import cl.bibliotecaescolar.model.RolUsuario;

/**
 *
 * Clase de utilidad para centralizar las validaciones de formularios en la capa de control (campos vacíos, RUTs, correos, etc.).
 */
public class ValidadorFormulario {

    /**
     *
     * Comprueba si alguno de los textos enviados es nulo o está vacío.
     * Aplica trim() a los campos de texto estándar.
     *
     * @param campos Cadena de textos a evaluar (varargs).
     * @return true si al menos un campo está vacío o nulo, false si todos contienen texto.
     */
    public static boolean hayCamposVacios(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Valida si la contraseña es nula, está vacía o contiene únicamente espacios en blanco (sin modificar la cadena original).
     *
     * @param contrasenia Contraseña a evaluar.
     * @return true si la contraseña no es válida, false si es correcta.
     */
    public static boolean esContraseniaInvalida(String contrasenia) {
        return contrasenia == null || contrasenia.isBlank();
    }

    /**
     *
     * Valida la presencia de datos básicos de una persona (RUT y Correo) junto con el formato de ambos.
     *
     * @param rut    RUT a evaluar.
     * @param correo Correo a evaluar.
     * @return Mensaje descriptivo con el error hallado, o null si todo es válido.
     */
    public static String validarPersona(String rut, String correo) {
        if (!ValidadorRut.esValido(rut)) {
            return "El RUT ingresado no tiene un formato válido (Ej: 12345678-9).";
        }

        if (!ValidadorCorreo.esValido(correo)) {
            return "El correo electrónico ingresado no tiene un formato válido (Ej: usuario@correo.cl).";
        }

        return null;
    }

    /**
     *
     * Valida todos los datos requeridos para la creación o actualización de un Usuario.
     * Mantiene la contraseña intacta sin recortar sus espacios.
     *
     * @param nombre      Nombre a evaluar.
     * @param rut         RUT a evaluar.
     * @param correo      Correo a evaluar.
     * @param contrasenia Contraseña a evaluar.
     * @param rol         Rol a evaluar.
     * @return Mensaje de error descriptivo o null si todo está en orden.
     */
    public static String validarUsuario(String nombre, String rut, String correo, String contrasenia, RolUsuario rol) {
        if (rol == null || esContraseniaInvalida(contrasenia) || hayCamposVacios(nombre, rut, correo)) {
            return "Todos los campos son obligatorios.";
        }
        return validarPersona(rut, correo);
    }
}