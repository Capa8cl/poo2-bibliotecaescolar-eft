package cl.bibliotecaescolar.util;

import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.model.ProcesadorPrestamos;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.model.RolUsuario;
import cl.bibliotecaescolar.view.VentanaLogin;
import cl.bibliotecaescolar.view.VentanaPrincipal;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * Representa la utilidades de apoyo para la GUI de la Biblioteca Escolar.
 */
public class UtilidadesGUI {

    /**
     * Configura los parámetros iniciales de la ventana (JFrame).
     *
     * @param ventana        Ventana que se va a configurar.
     * @param panelPrincipal Contenedor principal de la ventana.
     * @param titulo         Título de la ventana.
     * @param ancho          Ancho ventana.
     * @param alto           Alto ventana.
     * @param modoCierre     Cierre.
     */
    public static void configurarVentana(JFrame ventana, JPanel panelPrincipal, String titulo, int ancho, int alto, int modoCierre) {
        if (ventana == null || panelPrincipal == null) {
            return;
        }

        ventana.setContentPane(panelPrincipal);
        ventana.setTitle(titulo);
        ventana.setSize(ancho, alto);
        ventana.setDefaultCloseOperation(modoCierre);
        ventana.setLocationRelativeTo(null);
    }

    /**
     *
     * Valida si el usuario es estudiante.
     *
     * @param usuario Usuario a validar.
     * @return Devuelve true si es estudiante, false si no lo es.
     */
    public static boolean esEstudiante(Usuario usuario) {
        return usuario != null && usuario.getRol() == RolUsuario.ESTUDIANTE;
    }

    /**
     *
     * Valida si el usuario es bibliotecario.
     *
     * @param usuario Usuario a validar.
     * @return Devuelve true si es bibliotecario, false si no lo es.
     */
    public static boolean esBibliotecario(Usuario usuario) {
        return usuario != null && usuario.getRol() == RolUsuario.BIBLIOTECARIO;
    }

    /**
     *
     * Fuerza a que la posición de la ventana quede en el centro al activarse (visible).
     *
     * @param ventana Objeto Ventana a centrar.
     */
    public static void centrarVentana(JFrame ventana) {
        if (ventana == null) return;

        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                ventana.setLocationRelativeTo(null);
            }
        });
    }

    /**
     *
     * Configura el comportamiento del botón de cierre ('X') y botones de navegación para volver al menú principal (Bibliotecario) o cerrar sesión al Login (Estudiante).
     *
     * @param ventana            JFrame actual que se desea gestionar.
     * @param prestamoController Instancia del controlador de préstamos.
     * @param procesador         Instancia del procesador multihilo.
     * @param usuarioAutenticado Usuario en sesión.
     */
    public static void configurarNavegacionRetorno(JFrame ventana, PrestamoController prestamoController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Runnable accionRetorno = () -> {
            if (usuarioAutenticado.getRol() == RolUsuario.BIBLIOTECARIO) {
                VentanaPrincipal principal = new VentanaPrincipal(prestamoController, procesador, usuarioAutenticado);
                principal.setVisible(true);
                ventana.dispose();
            } else {
                int opcion = JOptionPane.showConfirmDialog(ventana, "¿Desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    VentanaLogin login = new VentanaLogin(prestamoController, procesador);
                    login.setVisible(true);
                    ventana.dispose();
                }
            }
        };

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                accionRetorno.run();
            }
        });
    }

    /**
     * Ejecuta el retorno hacia el menú o login según el rol del usuario.
     */
    public static void volverAnterior(JFrame ventanaActual, PrestamoController prestamoController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        if (usuarioAutenticado.getRol() == RolUsuario.BIBLIOTECARIO) {
            VentanaPrincipal principal = new VentanaPrincipal(prestamoController, procesador, usuarioAutenticado);
            principal.setVisible(true);
            ventanaActual.dispose();
        } else {
            int opcion = JOptionPane.showConfirmDialog(ventanaActual, "¿Desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                VentanaLogin login = new VentanaLogin(prestamoController, procesador);
                login.setVisible(true);
                ventanaActual.dispose();
            }
        }
    }
}
