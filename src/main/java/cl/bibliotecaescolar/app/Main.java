package cl.bibliotecaescolar.app;

import cl.bibliotecaescolar.config.ConexionDB;
import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.model.ProcesadorPrestamos;
import cl.bibliotecaescolar.view.VentanaLogin;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * Sistema de Gestión de Biblioteca Escolar.
 *
 * @author Fabrizio Fernandini
 * @version 1.9
 */
public class Main {

    /**
     *
     * Clase principal del sistema de Biblioteca Escolar.
     *
     * @param args Argumentos.
     */
    public static void main(String[] args) {
        try {
            Connection conn = ConexionDB.getInstance();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("No se pudo establecer la conexión activa con la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión:\nNo se pudo conectar a la base de datos de la biblioteca.\nVerifique que MySQL esté encendido y las credenciales sean correctas.\n\nDetalles: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ProcesadorPrestamos procesadorPrestamos = new ProcesadorPrestamos();
            PrestamoController prestamoController = new PrestamoController(procesadorPrestamos);

            VentanaLogin ventanaLogin = new VentanaLogin(prestamoController, procesadorPrestamos);
            ventanaLogin.setVisible(true);
        });
    }
}
