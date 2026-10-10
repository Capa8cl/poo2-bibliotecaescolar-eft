package cl.bibliotecaescolar.view;

import cl.bibliotecaescolar.controller.LibroController;
import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.model.CategoriaLibro;
import cl.bibliotecaescolar.model.Libro;
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
 * Representa la Ventana de Gestión del Catálogo de Libros (GUI).
 * Extiende de JFrame.
 */
public class GestionLibros extends JFrame {
    private final PrestamoController prestamoController;
    private final LibroController libroController;
    private final ProcesadorPrestamos procesador;
    private final Usuario usuarioAutenticado;

    private JPanel panelLibros;
    private JPanel panelPrincipal;
    private JPanel panelSuperior;
    private JPanel panelInfoPrestamos;
    private JPanel panelFormulario;

    private JLabel txtTitulo;
    private JLabel txtSubt1;
    private JLabel txtSubt3;

    private JTextField inputTitulo;
    private JTextField inputAutor;
    private JTextField inputISBN;
    private JTextField inputEditorial;
    private JSpinner spinnerStock;
    private JComboBox<CategoriaLibro> cbxCategoria;

    private JButton btnRegistrar;
    private JButton btnStock;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnActualizar;
    private JButton btnVolverMenu;

    private JTable tablaLibros;
    private JTable tablaPrestamos;

    /**
     *
     * Constructor de la ventana de gestión de libros.
     *
     * @param prestamoController Instancia del controlador de préstamos.
     * @param libroController    Instancia del controlador de libros.
     * @param procesador         Procesador de cola multihilo.
     * @param usuarioAutenticado Usuario activo en sesión.
     */
    public GestionLibros(PrestamoController prestamoController, LibroController libroController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        this.prestamoController = prestamoController;
        this.libroController = libroController;
        this.procesador = procesador;
        this.usuarioAutenticado = usuarioAutenticado;

        configuracion();
        cargarCategorias();
        cargarTablaLibros();
        configurarPermisosPorRol();
        eventos();
        UtilidadesGUI.centrarVentana(this);
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGUI.configurarVentana(this, panelLibros != null ? panelLibros : panelPrincipal, "Biblioteca Escolar | Gestión del Catálogo de Libros", 720, 550, JFrame.DO_NOTHING_ON_CLOSE);

        if (spinnerStock != null) {
            spinnerStock.setModel(new SpinnerNumberModel(1, 0, 999, 1));
        }

        UtilidadesGUI.configurarNavegacionRetorno(this, prestamoController, procesador, usuarioAutenticado);
    }

    /**
     *
     * Carga las categorías en el ComboBox desplegable usando el Enum.
     */
    private void cargarCategorias() {
        if (cbxCategoria == null) return;

        cbxCategoria.removeAllItems();
        for (CategoriaLibro cat : CategoriaLibro.values()) {
            cbxCategoria.addItem(cat);
        }
    }

    /**
     *
     * Carga y actualiza la JTable con la lista completa de libros desde LibroController.
     */
    private void cargarTablaLibros() {
        if (tablaLibros == null) return;

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Libro> lista = libroController.listarLibros();

        if (lista != null && !lista.isEmpty()) {
            for (Libro l : lista) {
                String nombreCategoria = CategoriaLibro.obtenerPorId(l.getIdCategoria()).getNombre();

                modelo.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.getEditorial(), l.getStock(), nombreCategoria});
            }
        } else {
            modelo.addRow(new Object[]{"-", "No hay libros registrados", "-", "-", "-", 0, "-"});
        }

        tablaLibros.setModel(modelo);
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(130);
        tablaLibros.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaLibros.getColumnModel().getColumn(4).setPreferredWidth(110);
        tablaLibros.getColumnModel().getColumn(5).setPreferredWidth(60);
        tablaLibros.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    /**
     *
     * Restringe opciones a usuarios con perfil de estudiante.
     */
    private void configurarPermisosPorRol() {
        if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
            if (btnRegistrar != null) btnRegistrar.setEnabled(false);
            if (btnStock != null) btnStock.setEnabled(false);
            if (btnEliminar != null) btnEliminar.setEnabled(false);
        }
    }

    /**
     *
     * Asignación de listeners a las variables exactas de los botones de la UI.
     */
    private void eventos() {
        if (btnRegistrar != null) {
            btnRegistrar.addActionListener((ActionEvent e) -> registrarNuevoLibro());
        }

        if (btnStock != null) {
            btnStock.addActionListener((ActionEvent e) -> ajustarStockLibro());
        }

        if (btnEliminar != null) {
            btnEliminar.addActionListener((ActionEvent e) -> eliminarLibroSeleccionado());
        }

        if (btnLimpiar != null) {
            btnLimpiar.addActionListener((ActionEvent e) -> limpiarFormulario());
        }

        if (btnActualizar != null) {
            btnActualizar.addActionListener((ActionEvent e) -> {
                cargarTablaLibros();
                JOptionPane.showMessageDialog(this, "Catálogo actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        if (btnVolverMenu != null) {
            btnVolverMenu.addActionListener((ActionEvent e) -> {
                UtilidadesGUI.volverAnterior(this, prestamoController, procesador, usuarioAutenticado);
            });
        }
    }

    /**
     *
     * Llama a LibroController.registrarLibro utilizando las validaciones de negocio.
     */
    private void registrarNuevoLibro() {
        String titulo = inputTitulo != null ? inputTitulo.getText().trim() : "";
        String autor = inputAutor != null ? inputAutor.getText().trim() : "";
        String isbn = inputISBN != null ? inputISBN.getText().trim() : "";
        String editorial = inputEditorial != null ? inputEditorial.getText().trim() : "";
        int stock = spinnerStock != null ? (int) spinnerStock.getValue() : 0;

        CategoriaLibro categoriaSeleccionada = (CategoriaLibro) cbxCategoria.getSelectedItem();
        int idCategoria = (categoriaSeleccionada != null) ? categoriaSeleccionada.getId() : 1;

        String resultado = libroController.registrarLibro(titulo, autor, isbn, editorial, stock, idCategoria);

        if (resultado.equals("Libro registrado exitosamente.")) {
            JOptionPane.showMessageDialog(this, resultado, "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaLibros();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * Pide la nueva cantidad de stock mediante un cuadro de diálogo y sobrescribe el valor actual.
     */
    private void ajustarStockLibro() {
        int fila = tablaLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para actualizar su stock.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = tablaLibros.getValueAt(fila, 0);
        if (!(idObj instanceof Integer)) return;

        int idLibro = (Integer) idObj;

        Libro libroActual = libroController.obtenerPorId(idLibro);
        if (libroActual == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener la información del libro.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String respuesta = JOptionPane.showInputDialog(this, "Ingrese el nuevo stock total para el libro:\n'" + libroActual.getTitulo() + "'", "Stock Actual: " + libroActual.getStock(), JOptionPane.QUESTION_MESSAGE);

        if (respuesta == null) {
            return;
        }

        try {
            int nuevoStock = Integer.parseInt(respuesta.trim());

            if (nuevoStock < 0) {
                JOptionPane.showMessageDialog(this, "El stock no puede ser un número negativo.", "Valor Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String resultado = libroController.actualizarLibro(libroActual.getId(), libroActual.getTitulo(), libroActual.getAutor(), libroActual.getIsbn(), libroActual.getEditorial(), nuevoStock, libroActual.getIdCategoria());

            if (resultado.equals("Libro actualizado correctamente.")) {
                JOptionPane.showMessageDialog(this, "Actualizado. Nuevo Stock: " + nuevoStock, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaLibros();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número entero válido para el stock.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * Llama a LibroController.eliminarLibro previa confirmación del usuario.
     */
    private void eliminarLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = tablaLibros.getValueAt(fila, 0);
        if (!(idObj instanceof Integer)) return;

        int idLibro = (Integer) idObj;

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el libro ID #" + idLibro + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = libroController.eliminarLibro(idLibro);
            if (resultado.startsWith("Libro eliminado")) {
                JOptionPane.showMessageDialog(this, resultado, "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaLibros();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     *
     * Reinicia los campos del formulario.
     */
    private void limpiarFormulario() {
        if (inputTitulo != null) inputTitulo.setText("");
        if (inputAutor != null) inputAutor.setText("");
        if (inputISBN != null) inputISBN.setText("");
        if (inputEditorial != null) inputEditorial.setText("");
        if (spinnerStock != null) spinnerStock.setValue(1);
        if (cbxCategoria != null && cbxCategoria.getItemCount() > 0) cbxCategoria.setSelectedIndex(0);
    }

}