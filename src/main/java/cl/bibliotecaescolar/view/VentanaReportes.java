package cl.bibliotecaescolar.view;

import cl.bibliotecaescolar.controller.EstudianteController;
import cl.bibliotecaescolar.controller.LibroController;
import cl.bibliotecaescolar.controller.PrestamoController;
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
import java.util.*;
import java.util.List;

/**
 *
 * Representa la Ventana de Informes y Reportes del Sistema (GUI).
 * Extiende de JFrame.
 */
public class VentanaReportes extends JFrame {
    private final PrestamoController prestamoController;
    private final LibroController libroController;
    private final EstudianteController estudianteController;
    private final ProcesadorPrestamos procesador;
    private final Usuario usuarioAutenticado;

    private JPanel panelReportes;
    private JPanel panelSuperior;
    private JPanel panelDatos;

    private JComboBox<String> cbxTipoReporte;
    private JComboBox<Object> cbxEstudiantes;

    private JButton btnGenerarReporte;
    private JButton btnLimpiar;
    private JButton btnVolverMenu;

    private JTable tablaReportes;

    /**
     *
     * Constructor de la ventana de reportes.
     *
     * @param prestamoController   Controlador de préstamos.
     * @param libroController      Controlador de libros.
     * @param estudianteController Controlador de estudiantes.
     * @param procesador           Procesador de cola multihilo.
     * @param usuarioAutenticado   Usuario activo en sesión.
     */
    public VentanaReportes(PrestamoController prestamoController, LibroController libroController, EstudianteController estudianteController, ProcesadorPrestamos procesador, Usuario usuarioAutenticado) {
        this.prestamoController = prestamoController;
        this.libroController = libroController;
        this.estudianteController = estudianteController;
        this.procesador = procesador;
        this.usuarioAutenticado = usuarioAutenticado;

        configuracion();
        cargarCombos();
        actualizarEstadoFiltros();
        eventos();
        UtilidadesGUI.centrarVentana(this);
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGUI.configurarVentana(this, panelReportes, "Biblioteca Escolar | Reportes e Informes", 750, 550, JFrame.DO_NOTHING_ON_CLOSE);
        UtilidadesGUI.configurarNavegacionRetorno(this, prestamoController, procesador, usuarioAutenticado);
    }

    /**
     *
     * Carga los tipos de reporte y la lista de estudiantes en los ComboBoxes.
     */
    private void cargarCombos() {
        if (cbxTipoReporte != null) {
            cbxTipoReporte.removeAllItems();
            cbxTipoReporte.addItem("Libros más prestados");
            cbxTipoReporte.addItem("Historial por estudiante");
            cbxTipoReporte.addItem("Libros actualmente en préstamo");
        }

        if (cbxEstudiantes != null) {
            cbxEstudiantes.removeAllItems();
            List<Estudiante> estudiantes = estudianteController.listarEstudiantes();
            if (estudiantes != null && !estudiantes.isEmpty()) {
                for (Estudiante e : estudiantes) {
                    if (usuarioAutenticado.getRol() == RolUsuario.ESTUDIANTE) {
                        if (e.getId() == usuarioAutenticado.getId()) {
                            cbxEstudiantes.addItem(e);
                            break;
                        }
                    } else {
                        cbxEstudiantes.addItem(e);
                    }
                }
            }
        }
    }

    /**
     *
     * Habilita o deshabilita el filtro de estudiantes según el tipo de reporte.
     */
    private void actualizarEstadoFiltros() {
        if (cbxTipoReporte == null || cbxEstudiantes == null) return;

        boolean esReporteHistorial = cbxTipoReporte.getSelectedIndex() == 1;
        cbxEstudiantes.setEnabled(esReporteHistorial && usuarioAutenticado.getRol() != RolUsuario.ESTUDIANTE);
    }

    /**
     *
     * Configura los eventos de los componentes visuales.
     */
    private void eventos() {
        if (cbxTipoReporte != null) {
            cbxTipoReporte.addActionListener((ActionEvent e) -> actualizarEstadoFiltros());
        }

        if (btnGenerarReporte != null) {
            btnGenerarReporte.addActionListener((ActionEvent e) -> generarReporte());
        }

        if (btnLimpiar != null) {
            btnLimpiar.addActionListener((ActionEvent e) -> limpiarVista());
        }

        if (btnVolverMenu != null) {
            btnVolverMenu.addActionListener((ActionEvent e) -> {
                UtilidadesGUI.volverAnterior(this, prestamoController, procesador, usuarioAutenticado);
            });
        }
    }

    /**
     *
     * Evalúa la opción seleccionada y genera el reporte correspondiente.
     */
    private void generarReporte() {
        int opcion = cbxTipoReporte != null ? cbxTipoReporte.getSelectedIndex() : 0;
        switch (opcion) {
            case 0 -> mostrarReporteMasPrestados();
            case 1 -> mostrarReporteHistorialEstudiante();
            case 2 -> mostrarReporteActualmentePrestados();
        }
    }

    /**
     *
     * Reinicia los campos de selección y limpia las filas de la JTable.
     */
    private void limpiarVista() {
        if (cbxTipoReporte != null && cbxTipoReporte.getItemCount() > 0) {
            cbxTipoReporte.setSelectedIndex(0);
        }
        if (cbxEstudiantes != null && cbxEstudiantes.getItemCount() > 0) {
            cbxEstudiantes.setSelectedIndex(0);
        }

        if (tablaReportes != null) {
            DefaultTableModel modeloVacio = new DefaultTableModel(new Object[]{"Información"}, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            modeloVacio.addRow(new Object[]{"Seleccione un reporte y presione 'Generar Reporte'"});
            tablaReportes.setModel(modeloVacio);
        }
    }

    /**
     *
     * Carga el reporte de libros más prestados ordenados por frecuencia.
     */
    private void mostrarReporteMasPrestados() {
        if (tablaReportes == null) return;

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Ranking", "ID Libro", "Título", "Autor", "Total Préstamos"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        List<Prestamo> prestamos = prestamoController.listarTodos();
        Map<Integer, Integer> conteoLibros = new HashMap<>();

        if (prestamos != null) {
            for (Prestamo p : prestamos) {
                conteoLibros.put(p.getIdLibro(), conteoLibros.getOrDefault(p.getIdLibro(), 0) + 1);
            }
        }

        List<Map.Entry<Integer, Integer>> listaOrdenada = new ArrayList<>(conteoLibros.entrySet());
        listaOrdenada.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        if (!listaOrdenada.isEmpty()) {
            int ranking = 1;
            for (Map.Entry<Integer, Integer> entry : listaOrdenada) {
                Libro libro = libroController.obtenerPorId(entry.getKey());
                String titulo = libro != null ? libro.getTitulo() : "Desconocido";
                String autor = libro != null ? libro.getAutor() : "Desconocido";

                modelo.addRow(new Object[]{"#" + ranking++, entry.getKey(), titulo, autor, entry.getValue()});
            }
        } else {
            modelo.addRow(new Object[]{"-", "-", "Sin préstamos registrados", "-", 0});
        }

        tablaReportes.setModel(modelo);
    }

    /**
     *
     * Carga el historial de préstamos del estudiante seleccionado.
     */
    private void mostrarReporteHistorialEstudiante() {
        if (tablaReportes == null) return;

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID Préstamo", "Libro", "Fecha Préstamo", "Fecha Devolución", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        Object seleccion = cbxEstudiantes != null ? cbxEstudiantes.getSelectedItem() : null;
        if (!(seleccion instanceof Estudiante e)) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante válido.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Prestamo> historial = prestamoController.listarPorEstudiante(e.getId());

        if (historial != null && !historial.isEmpty()) {
            for (Prestamo p : historial) {
                Libro l = libroController.obtenerPorId(p.getIdLibro());
                String tituloLibro = l != null ? l.getTitulo() : "ID #" + p.getIdLibro();

                String estado = p.isDevuelto() ? "Devuelto" : (LocalDate.now().isAfter(p.getFechaDevolucion()) ? "Atrasado" : "Vigente");

                modelo.addRow(new Object[]{p.getId(), tituloLibro, p.getFechaPrestamo(), p.getFechaDevolucion(), estado});
            }
        } else {
            modelo.addRow(new Object[]{"-", "Sin historial registrado", "-", "-", "-"});
        }

        tablaReportes.setModel(modelo);
    }

    /**
     * Carga la lista de libros en préstamo actualmente activos.
     */
    private void mostrarReporteActualmentePrestados() {
        if (tablaReportes == null) return;

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID Préstamo", "Estudiante", "Libro", "Fecha Devolución", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        List<Prestamo> activos = prestamoController.listarPrestamosActivos();

        if (activos != null && !activos.isEmpty()) {
            for (Prestamo p : activos) {
                Estudiante est = estudianteController.obtenerPorId(p.getIdEstudiante());
                Libro lib = libroController.obtenerPorId(p.getIdLibro());

                String nombreEstudiante = est != null ? est.getNombre() : "ID #" + p.getIdEstudiante();
                String tituloLibro = lib != null ? lib.getTitulo() : "ID #" + p.getIdLibro();

                String situacion = LocalDate.now().isAfter(p.getFechaDevolucion()) ? "Atrasado" : "Vigente";

                modelo.addRow(new Object[]{p.getId(), nombreEstudiante, tituloLibro, p.getFechaDevolucion(), situacion});
            }
        } else {
            modelo.addRow(new Object[]{"-", "No hay préstamos activos", "-", "-", "-"});
        }

        tablaReportes.setModel(modelo);
    }

}