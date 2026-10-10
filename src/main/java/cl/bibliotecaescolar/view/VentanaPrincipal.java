package cl.bibliotecaescolar.view;

import cl.bibliotecaescolar.controller.EstudianteController;
import cl.bibliotecaescolar.controller.LibroController;
import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.model.ProcesadorPrestamos;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.model.RolUsuario;
import cl.bibliotecaescolar.util.UtilidadesGUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

/**
 *
 * Representa la Ventana Principal (GUI) tras iniciar sesión en la Biblioteca Escolar con rol Bibliotecario.
 * Extiende de JFrame.
 */
public class VentanaPrincipal extends JFrame {
    private final PrestamoController prestamoController;
    private final ProcesadorPrestamos procesador;
    private final Usuario usuarioAutenticado;

    private JPanel panelPrincipal;
    private JButton btnGestionLibros;
    private JButton btnGestionEstudiantes;
    private JButton btnReportes;
    private JButton btnGestionPrestamos;
    private JButton btnCerrarSesion;
    private JPanel panelSuperior;
    private JPanel panelBotones;

    /**
     *
     * Constructor de la Ventana Principal.
     *
     * @param prestamoController Controlador de préstamos.
     * @param procesador         Cola de procesamiento concurrente.
     * @param usuarioAutenticado Usuario que inició sesión.
     */
    public VentanaPrincipal(PrestamoController prestamoController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        this.prestamoController = prestamoController;
        this.procesador = procesador;
        this.usuarioAutenticado = usuarioAutenticado;

        configuracion();
        configurarPermisosPorRol();
        eventos();
        UtilidadesGUI.centrarVentana(this);
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGUI.configurarVentana(this, panelPrincipal, "Biblioteca Escolar | EFT", 600, 400, JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     * Aplica restricciones visuales según el rol del usuario.
     */
    private void configurarPermisosPorRol() {
        if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
            if (btnGestionLibros != null) btnGestionLibros.setEnabled(false);
            if (btnGestionEstudiantes != null) btnGestionEstudiantes.setEnabled(false);
            if (btnReportes != null) btnReportes.setEnabled(false);
        }
    }

    /**
     *
     * Configura los eventos de los botones de navegación.
     */
    private void eventos() {
        if (btnGestionLibros != null) {
            btnGestionLibros.addActionListener((ActionEvent e) -> {
                LibroController libroController = new LibroController();
                GestionLibros ventanaLibros = new GestionLibros(prestamoController, libroController, procesador, usuarioAutenticado);
                ventanaLibros.setVisible(true);
                this.dispose();
            });
        }

        if (btnGestionEstudiantes != null) {
            btnGestionEstudiantes.addActionListener((ActionEvent e) -> {
                EstudianteController estudianteController = new EstudianteController();
                GestionEstudiantes ventanaEstudiantes = new GestionEstudiantes(prestamoController, estudianteController, procesador, usuarioAutenticado);
                ventanaEstudiantes.setVisible(true);
                this.dispose();
            });
        }

        if (btnGestionPrestamos != null) {
            btnGestionPrestamos.addActionListener((ActionEvent e) -> {
                GestionPrestamos ventanaPrestamos = new GestionPrestamos(prestamoController, procesador, usuarioAutenticado);
                ventanaPrestamos.setVisible(true);
                this.dispose();
            });
        }

        if (btnReportes != null) {
            btnReportes.addActionListener((ActionEvent e) -> {
                LibroController libroController = new LibroController();
                EstudianteController estudianteController = new EstudianteController();

                VentanaReportes ventanaReportes = new VentanaReportes(prestamoController, libroController, estudianteController, procesador, usuarioAutenticado);
                ventanaReportes.setVisible(true);
                this.dispose();
            });
        }

        if (btnCerrarSesion != null) {
            btnCerrarSesion.addActionListener((ActionEvent e) -> {
                int opcion = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión y volver al login?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    VentanaLogin login = new VentanaLogin(prestamoController, procesador);
                    login.setVisible(true);
                    this.dispose();
                }
            });
        }
    }

}