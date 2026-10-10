package cl.bibliotecaescolar.view;

import cl.bibliotecaescolar.controller.EstudianteController;
import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.model.Estudiante;
import cl.bibliotecaescolar.model.ProcesadorPrestamos;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.model.RolUsuario;
import cl.bibliotecaescolar.util.UtilidadesGUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Locale;

/**
 *
 * Representa la Ventana de Gestión de Estudiantes (GUI).
 * Extiende de JFrame.
 */
public class GestionEstudiantes extends JFrame {
    private final PrestamoController prestamoController;
    private final EstudianteController estudianteController;
    private final ProcesadorPrestamos procesador;
    private final Usuario usuarioAutenticado;

    private JPanel panelEstudiantes;
    private JTextField inputNombre;
    private JTextField inputCurso;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVolverMenu;
    private JPanel panelPrincipal;
    private JLabel txtTitulo;
    private JPanel panelInfoEstudiantes;
    private JLabel txtSubt1;
    private JLabel txtSubt3;
    private JTextField inputRUT;
    private JTextField inputCorreo;

    private JTable tablaEstudiantes;
    private JPanel panelSuperior;

    /**
     *
     * Constructor de la ventana de gestión de estudiantes.
     *
     * @param prestamoController   Instancia del controlador de préstamos.
     * @param estudianteController Instancia del controlador de estudiantes.
     * @param procesador           Procesador de cola multihilo.
     * @param usuarioAutenticado   Usuario activo en sesión.
     */
    public GestionEstudiantes(PrestamoController prestamoController, EstudianteController estudianteController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        this.prestamoController = prestamoController;
        this.estudianteController = estudianteController;
        this.procesador = procesador;
        this.usuarioAutenticado = usuarioAutenticado;

        configuracion();
        cargarTablaEstudiantes();
        configurarPermisosPorRol();
        eventos();
        UtilidadesGUI.centrarVentana(this);
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGUI.configurarVentana(this, panelEstudiantes, "Biblioteca Escolar | Gestión de Estudiantes", 720, 550, JFrame.DO_NOTHING_ON_CLOSE);

        UtilidadesGUI.configurarNavegacionRetorno(this, prestamoController, procesador, usuarioAutenticado);
    }

    /**
     *
     * Carga y actualiza la JTable con la lista de estudiantes.
     */
    private void cargarTablaEstudiantes() {
        if (tablaEstudiantes == null) return;

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "RUT", "Email", "Curso"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Estudiante> lista = (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) ? List.of(estudianteController.obtenerPorId(usuarioAutenticado.getId())) : estudianteController.listarEstudiantes();

        if (lista != null && !lista.isEmpty() && lista.get(0) != null) {
            for (Estudiante e : lista) {
                modelo.addRow(new Object[]{e.getId(), e.getNombre(), e.getRut(), e.getEmail(), e.getCurso()});
            }
        } else {
            modelo.addRow(new Object[]{"-", "No hay estudiantes registrados", "-", "-", "-"});
        }

        tablaEstudiantes.setModel(modelo);
        tablaEstudiantes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tablaEstudiantes.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaEstudiantes.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablaEstudiantes.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaEstudiantes.getColumnModel().getColumn(3).setPreferredWidth(140);
        tablaEstudiantes.getColumnModel().getColumn(4).setPreferredWidth(140);
    }

    /**
     *
     * Restringe opciones si el usuario logueado es estudiante.
     */
    private void configurarPermisosPorRol() {
        if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
            if (btnRegistrar != null) btnRegistrar.setEnabled(false);
            if (btnActualizar != null) btnActualizar.setEnabled(false);
            if (btnEliminar != null) btnEliminar.setEnabled(false);
        }
    }

    /**
     *
     * Asignación de Listeners a los botones.
     */
    private void eventos() {
        if (btnRegistrar != null) {
            btnRegistrar.addActionListener((ActionEvent e) -> registrarEstudiante());
        }

        if (btnActualizar != null) {
            btnActualizar.addActionListener((ActionEvent e) -> {
                cargarTablaEstudiantes();
                JOptionPane.showMessageDialog(this, "Lista de estudiantes actualizada correctamente.", "Tabla Refrescada", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        if (btnEliminar != null) {
            btnEliminar.addActionListener((ActionEvent e) -> eliminarEstudiante());
        }

        if (btnLimpiar != null) {
            btnLimpiar.addActionListener((ActionEvent e) -> limpiarFormulario());
        }

        if (btnVolverMenu != null) {
            btnVolverMenu.addActionListener((ActionEvent e) -> {
                UtilidadesGUI.volverAnterior(this, prestamoController, procesador, usuarioAutenticado);
            });
        }
    }

    /**
     *
     * Registra un nuevo estudiante a través de EstudianteController
     * integrándolo automáticamente como usuario del sistema.
     */
    private void registrarEstudiante() {
        String nombre = inputNombre != null ? inputNombre.getText().trim() : "";
        String rut = inputRUT != null ? inputRUT.getText().trim() : "";
        String email = inputCorreo != null ? inputCorreo.getText().trim() : "";
        String curso = inputCurso != null ? inputCurso.getText().trim() : "";

        String resultado = estudianteController.registrarEstudiante(nombre, rut, email, curso);

        if (resultado.startsWith("Estudiante registrado")) {
            JOptionPane.showMessageDialog(this, resultado, "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaEstudiantes();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error al Registrar", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * Edita los datos del estudiante seleccionado en la tabla.
     */
    private void actualizarEstudiante() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = tablaEstudiantes.getValueAt(fila, 0);
        if (!(idObj instanceof Integer)) return;

        int idEstudiante = (Integer) idObj;
        String nombre = inputNombre != null ? inputNombre.getText().trim() : "";
        String rut = inputRUT != null ? inputRUT.getText().trim() : "";
        String email = inputCorreo != null ? inputCorreo.getText().trim() : "";
        String curso = inputCurso != null ? inputCurso.getText().trim() : "";

        String resultado = estudianteController.actualizarEstudiante(idEstudiante, nombre, rut, email, curso);

        if (resultado.startsWith("Estudiante actualizado")) {
            JOptionPane.showMessageDialog(this, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaEstudiantes();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error de Actualización", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * Elimina al estudiante seleccionado tras confirmación.
     */
    private void eliminarEstudiante() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante para eliminar.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = tablaEstudiantes.getValueAt(fila, 0);
        if (!(idObj instanceof Integer)) return;

        int idEstudiante = (Integer) idObj;

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al estudiante ID #" + idEstudiante + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = estudianteController.eliminarEstudiante(idEstudiante);
            if (resultado.startsWith("Estudiante eliminado")) {
                JOptionPane.showMessageDialog(this, resultado, "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaEstudiantes();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     *
     * Limpia las entradas del formulario.
     */
    private void limpiarFormulario() {
        if (inputNombre != null) inputNombre.setText("");
        if (inputRUT != null) inputRUT.setText("");
        if (inputCorreo != null) inputCorreo.setText("");
        if (inputCurso != null) inputCurso.setText("");
    }

}