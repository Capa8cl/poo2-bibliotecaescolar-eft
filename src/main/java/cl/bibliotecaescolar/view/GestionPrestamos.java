package cl.bibliotecaescolar.view;

import cl.bibliotecaescolar.controller.PrestamoController;
import cl.bibliotecaescolar.model.Bibliotecario;
import cl.bibliotecaescolar.model.Estudiante;
import cl.bibliotecaescolar.model.Libro;
import cl.bibliotecaescolar.model.Prestamo;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 *
 * Representa la Ventana Gestión de Préstamos y Devoluciones (GUI).
 * Extiende de JFrame.
 */
public class GestionPrestamos extends JFrame {
    private final PrestamoController prestamoController;
    private final ProcesadorPrestamos procesador;
    private final Usuario usuarioAutenticado;

    private JPanel panelPrestamos;
    private JPanel panelPrincipal;
    private JPanel panelSuperior;
    private JPanel panelInfoPrestamos;
    private JLabel txtTitulo;
    private JLabel txtSubt1;
    private JLabel txtSubt2;
    private JLabel txtSubt3;
    private JComboBox<Object> cbxLibros;
    private JComboBox<Object> cbxEstudiantes;
    private JButton btnRegistrarPrestamo;

    private JComboBox<Object> cbxPrestamoDevolucion;
    private JButton btnRegistrarDevolucion;

    private JTable tablaPrestamos;
    private JButton btnVolverMenu;
    private JButton btnActualizar;

    /**
     *
     * Constructor de la ventana de gestión de préstamos.
     */
    public GestionPrestamos(PrestamoController prestamoController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        this.prestamoController = prestamoController;
        this.procesador = procesador;
        this.usuarioAutenticado = usuarioAutenticado;

        configuracion();
        cargarDatosComboBox();
        cargarTablaPrestamos();
        configurarPermisosPorRol();
        eventos();
        UtilidadesGUI.centrarVentana(this);
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGUI.configurarVentana(this, panelPrestamos, "Biblioteca Escolar | Gestión de Préstamos y Devoluciones", 780, 550, JFrame.DO_NOTHING_ON_CLOSE);

        if (btnVolverMenu != null) {
            if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
                btnVolverMenu.setText("Cerrar Sesión");
            } else {
                btnVolverMenu.setText("Volver al Menú");
            }
        }

        UtilidadesGUI.configurarNavegacionRetorno(this, prestamoController, procesador, usuarioAutenticado);
    }

    /**
     *
     * Carga los objetos desde la base de datos a los ComboBox.
     */
    private void cargarDatosComboBox() {
        boolean hayLibros = false;
        if (cbxLibros != null) {
            cbxLibros.removeAllItems();
            List<Libro> libros = prestamoController.obtenerLibrosDisponibles();
            if (libros != null && !libros.isEmpty()) {
                for (Libro l : libros) {
                    cbxLibros.addItem(l);
                }
                cbxLibros.setEnabled(true);
                hayLibros = true;
            } else {
                cbxLibros.addItem("Sin libros disponibles");
                cbxLibros.setEnabled(false);
            }
        }

        boolean hayEstudiantes = false;
        if (cbxEstudiantes != null) {
            cbxEstudiantes.removeAllItems();
            List<Estudiante> estudiantes = prestamoController.obtenerEstudiantes();

            if (estudiantes != null && !estudiantes.isEmpty()) {
                for (Estudiante e : estudiantes) {
                    if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
                        boolean coincideEmail = e.getEmail() != null && e.getEmail().equalsIgnoreCase(usuarioAutenticado.getEmail());
                        boolean coincideRut = e.getRut() != null && e.getRut().equalsIgnoreCase(usuarioAutenticado.getRut());
                        boolean coincideId = e.getId() == usuarioAutenticado.getId();

                        if (coincideEmail || coincideRut || coincideId) {
                            cbxEstudiantes.addItem(e);
                            hayEstudiantes = true;
                            break;
                        }
                    } else {
                        cbxEstudiantes.addItem(e);
                        hayEstudiantes = true;
                    }
                }
            }

            if (hayEstudiantes) {
                if (usuarioAutenticado.getRol() != RolUsuario.ESTUDIANTE) {
                    cbxEstudiantes.setEnabled(true);
                } else {
                    cbxEstudiantes.setEnabled(false);
                }
            } else {
                cbxEstudiantes.addItem("Sin estudiantes registrados");
                cbxEstudiantes.setEnabled(false);
            }
        }

        if (btnRegistrarPrestamo != null) {
            btnRegistrarPrestamo.setEnabled(hayLibros && hayEstudiantes);
        }

        actualizarComboPrestamosActivos();
    }

    /**
     *
     * Actualiza la lista de préstamos pendientes en el ComboBox de devolución.
     */
    private void actualizarComboPrestamosActivos() {
        if (cbxPrestamoDevolucion != null) {
            cbxPrestamoDevolucion.removeAllItems();
            List<Prestamo> prestamosActivos = prestamoController.listarPrestamosActivos();

            int idEstudianteSesion = obtenerIdEstudianteSesion();

            if (prestamosActivos != null && !prestamosActivos.isEmpty()) {
                boolean hayElementos = false;
                for (Prestamo p : prestamosActivos) {
                    if (!p.isDevuelto()) {
                        if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
                            if (p.getIdEstudiante() == idEstudianteSesion) {
                                cbxPrestamoDevolucion.addItem(p);
                                hayElementos = true;
                            }
                        } else {
                            cbxPrestamoDevolucion.addItem(p);
                            hayElementos = true;
                        }
                    }
                }

                if (hayElementos) {
                    cbxPrestamoDevolucion.setEnabled(true);
                    if (btnRegistrarDevolucion != null) btnRegistrarDevolucion.setEnabled(true);
                } else {
                    cbxPrestamoDevolucion.addItem("No hay préstamos activos");
                    cbxPrestamoDevolucion.setEnabled(false);
                    if (btnRegistrarDevolucion != null) btnRegistrarDevolucion.setEnabled(false);
                }
            } else {
                cbxPrestamoDevolucion.addItem("No hay préstamos activos");
                cbxPrestamoDevolucion.setEnabled(false);
                if (btnRegistrarDevolucion != null) btnRegistrarDevolucion.setEnabled(false);
            }
        }
    }

    /**
     *
     * Carga y refresca la JTable con los nombres de libro y estudiante en lugar de IDs.
     */
    private void cargarTablaPrestamos() {
        if (tablaPrestamos == null) return;

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Libro", "Estudiante", "F. Préstamo", "F. Vencimiento", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int idEstudianteSesion = obtenerIdEstudianteSesion();

        List<Prestamo> lista = (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) ? prestamoController.listarPorEstudiante(idEstudianteSesion) : prestamoController.listarTodos();
        List<Libro> libros = prestamoController.obtenerLibrosDisponibles();
        List<Estudiante> estudiantes = prestamoController.obtenerEstudiantes();

        LocalDate hoy = LocalDate.now();

        if (lista != null && !lista.isEmpty()) {
            for (Prestamo p : lista) {
                String estado;
                if (p.isDevuelto()) {
                    estado = "Devuelto";
                } else if (p.getFechaDevolucion() != null && hoy.isAfter(p.getFechaDevolucion())) {
                    estado = "Atrasado";
                } else {
                    estado = "Vigente";
                }

                String nombreLibro = "Libro ID: " + p.getIdLibro();
                if (libros != null) {
                    for (Libro l : libros) {
                        if (l.getId() == p.getIdLibro()) {
                            nombreLibro = l.getTitulo();
                            break;
                        }
                    }
                }

                String nombreEstudiante = "Estudiante ID: " + p.getIdEstudiante();
                if (estudiantes != null) {
                    for (Estudiante e : estudiantes) {
                        if (e.getId() == p.getIdEstudiante()) {
                            nombreEstudiante = e.getNombre();
                            break;
                        }
                    }
                }

                modelo.addRow(new Object[]{p.getId(), nombreLibro, nombreEstudiante, p.getFechaPrestamo(), p.getFechaDevolucion(), estado});
            }
        } else {
            modelo.addRow(new Object[]{"-", "-", "-", "-", "-", "No hay préstamos registrados"});
        }

        tablaPrestamos.setModel(modelo);
        tablaPrestamos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tablaPrestamos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaPrestamos.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaPrestamos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaPrestamos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaPrestamos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaPrestamos.getColumnModel().getColumn(5).setPreferredWidth(100);
    }

    /**
     *
     * Busca en la lista de estudiantes el ID real que corresponde al usuario autenticado.
     */
    private int obtenerIdEstudianteSesion() {
        List<Estudiante> estudiantes = prestamoController.obtenerEstudiantes();
        if (estudiantes != null) {
            for (Estudiante e : estudiantes) {
                if ((e.getEmail() != null && e.getEmail().equalsIgnoreCase(usuarioAutenticado.getEmail())) || (e.getRut() != null && e.getRut().equalsIgnoreCase(usuarioAutenticado.getRut()))) {
                    return e.getId();
                }
            }
        }
        return usuarioAutenticado.getId();
    }

    /**
     *
     * Restricción por rol de usuario.
     */
    private void configurarPermisosPorRol() {
        if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
            if (cbxEstudiantes != null) {
                cbxEstudiantes.setEnabled(false);
            }
        }
    }

    /**
     *
     * Eventos de la ventana.
     */
    private void eventos() {
        if (btnRegistrarPrestamo != null) {
            btnRegistrarPrestamo.addActionListener((ActionEvent e) -> registrarPrestamo());
        }

        if (btnRegistrarDevolucion != null) {
            btnRegistrarDevolucion.addActionListener((ActionEvent e) -> registrarDevolucion());
        }

        if (btnActualizar != null) {
            btnActualizar.addActionListener((ActionEvent e) -> {
                refrescarVista();
                JOptionPane.showMessageDialog(this, "Tabla y registros actualizados correctamente.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
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
     * Registra el préstamo validando stock y actualizando la interfaz.
     */
    private void registrarPrestamo() {
        Object itemLibro = cbxLibros.getSelectedItem();
        Object itemEstudiante = cbxEstudiantes.getSelectedItem();

        if (!(itemLibro instanceof Libro) || !(itemEstudiante instanceof Estudiante)) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro y un estudiante válido.", "Selección Incompleta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Libro libroSeleccionado = (Libro) itemLibro;
        Estudiante estudianteSeleccionado = (Estudiante) itemEstudiante;

        if (libroSeleccionado.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "El libro '" + libroSeleccionado.getTitulo() + "' no tiene stock disponible.", "Sin Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioAutenticado.getRol() == RolUsuario.BIBLIOTECARIO) {
            Bibliotecario bibliotecario = new Bibliotecario(usuarioAutenticado.getId(), usuarioAutenticado.getNombre(), usuarioAutenticado.getRut(), usuarioAutenticado.getEmail(), procesador);
            prestamoController.iniciarProcesamiento(bibliotecario);
        }

        String resultado = prestamoController.registrarPrestamo(estudianteSeleccionado.getId(), libroSeleccionado.getId(), 0);

        if (resultado.startsWith("Préstamo registrado correctamente")) {
            JOptionPane.showMessageDialog(this, resultado, "Préstamo Registrado", JOptionPane.INFORMATION_MESSAGE);
            refrescarVista();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error al Procesar Préstamo", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * Procesa la devolución calculando la fecha actual contra el vencimiento.
     */
    private void registrarDevolucion() {
        Object seleccion = cbxPrestamoDevolucion.getSelectedItem();

        if (seleccion == null || !(seleccion instanceof Prestamo)) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo válido para devolver.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Prestamo prestamoSeleccionado = (Prestamo) seleccion;

        if (prestamoSeleccionado.isDevuelto()) {
            JOptionPane.showMessageDialog(this, "Este préstamo ya ha sido registrado como devuelto.", "Préstamo Finalizado", JOptionPane.WARNING_MESSAGE);
            refrescarVista();
            return;
        }

        boolean estabaAtrasado = LocalDate.now().isAfter(prestamoSeleccionado.getFechaDevolucion());

        int idPrestamo = prestamoSeleccionado.getId();
        String resultado = prestamoController.registrarDevolucion(idPrestamo);

        if (resultado.startsWith("Devolución registrada correctamente")) {
            String mensajeDetalle = resultado;
            if (estabaAtrasado) {
                mensajeDetalle += "\nLa devolución se ha registrado con atraso (Fecha vencida).";
            }

            JOptionPane.showMessageDialog(this, mensajeDetalle, "Devolución Exitosa", estabaAtrasado ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
            refrescarVista();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error en Devolución", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * Reutilizable para actualizar las listas desplegables (ComboBoxes) y la tabla de préstamos.
     */
    private void refrescarVista() {
        cargarDatosComboBox();
        cargarTablaPrestamos();
    }

}