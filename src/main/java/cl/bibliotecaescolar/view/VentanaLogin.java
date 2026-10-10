package cl.bibliotecaescolar.view;

import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.controller.UsuarioController;
import cl.bibliotecaescolar.model.ProcesadorPrestamos;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.model.RolUsuario;
import cl.bibliotecaescolar.util.UtilidadesGUI;
import cl.bibliotecaescolar.util.ValidadorCorreo;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

/**
 *
 * Representa la Ventana Login (GUI) para el inicio de sesión en la Biblioteca Escolar.
 * Extiende de JFrame.
 */
public class VentanaLogin extends JFrame {
    private final UsuarioController usuarioController;
    private final PrestamoController prestamoController;
    private final ProcesadorPrestamos procesador;
    private JPanel panelLogin;
    private JButton btnIniciarSesion;
    private JPanel panelSuperior;
    private JPanel panelIniciarSesion;
    private JTextField inputCorreo;
    private JLabel txtTitulo;
    private JPanel panelSubtitulo;
    private JLabel txtSubtitulo;
    private JPanel panelSesion;
    private JLabel txtCorreo;
    private JLabel txtContrasenia;
    private JPasswordField passwordContrasenia;

    /**
     *
     * Constructor de la Ventana de Login.
     *
     * @param prestamoController Instancia del controlador de préstamos.
     * @param procesador         Instancia del procesador multihilo.
     */
    public VentanaLogin(PrestamoController prestamoController, ProcesadorPrestamos procesador) {
        this.usuarioController = new UsuarioController();
        this.prestamoController = prestamoController;
        this.procesador = procesador;

        configuracion();
        eventos();
        UtilidadesGUI.centrarVentana(this);
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGUI.configurarVentana(this, panelLogin, "Biblioteca Escolar | EFT", 300, 250, JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     * Valida los campos y procesa la autenticación con el controlador.
     */
    private void iniciarSesion() {
        String correo = inputCorreo.getText().trim();
        String contrasenia = new String(passwordContrasenia.getPassword());

        if (correo.isEmpty() || contrasenia.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidadorCorreo.esValido(correo)) {
            JOptionPane.showMessageDialog(this, "El formato del correo electrónico no es válido.", "Formato Incorrecto", JOptionPane.WARNING_MESSAGE);
            inputCorreo.requestFocus();
            return;
        }

        String correoLimpio = ValidadorCorreo.limpiarFormato(correo);

        Usuario usuarioAutenticado = usuarioController.iniciarSesion(correoLimpio, contrasenia);

        if (usuarioAutenticado != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido/a, " + usuarioAutenticado.getNombre() + "!\nRol: " + usuarioAutenticado.getRol(), "Ingreso correcto", JOptionPane.INFORMATION_MESSAGE);

            if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
                GestionPrestamos gestionPrestamos = new GestionPrestamos(prestamoController, procesador, usuarioAutenticado);
                gestionPrestamos.setVisible(true);
            } else {
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(prestamoController, procesador, usuarioAutenticado);
                ventanaPrincipal.setVisible(true);
            }

            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Inicio de sesión incorrecto o usuario no registrado.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            passwordContrasenia.setText("");
            passwordContrasenia.requestFocus();
        }
    }

    /**
     *
     * Eventos de la ventana.
     */
    private void eventos() {
        btnIniciarSesion.addActionListener((ActionEvent e) -> iniciarSesion());

        inputCorreo.addActionListener((ActionEvent e) -> iniciarSesion());
        passwordContrasenia.addActionListener((ActionEvent e) -> iniciarSesion());
    }

}