package cl.bibliotecaescolar.dao.impl;

import cl.bibliotecaescolar.config.ConexionDB;
import cl.bibliotecaescolar.dao.PrestamoDAO;
import cl.bibliotecaescolar.model.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implementación de la interface PrestamoDAO para la gestión de persistencia en MySQL.
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    /**
     *
     * Guarda un nuevo préstamo en la base de datos.
     *
     * @param prestamo Objeto Prestamo a registrar.
     * @return true si fue guardado o false en caso contrario.
     */
    @Override
    public boolean guardar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, prestamo.getIdEstudiante());
                ps.setInt(2, prestamo.getIdLibro());
                ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
                ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
                ps.setBoolean(5, prestamo.isDevuelto());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al guardar préstamo: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al guardar préstamo: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Obtiene un préstamo específico de la base de datos a partir de su ID.
     *
     * @param id ID del préstamo a buscar.
     * @return Objeto Prestamo si se encuentra, o null si no existe.
     */
    @Override
    public Prestamo obtenerPorId(int id) {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Prestamo(rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"), rs.getDate("fecha_prestamo").toLocalDate(), rs.getDate("fecha_devolucion").toLocalDate(), rs.getBoolean("devuelto"));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener préstamo por ID: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al obtener préstamo por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Obtiene la lista completa de todos los préstamos registrados.
     *
     * @return Lista de objetos Prestamo.
     */
    @Override
    public List<Prestamo> listarTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Prestamo(rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"), rs.getDate("fecha_prestamo").toLocalDate(), rs.getDate("fecha_devolucion").toLocalDate(), rs.getBoolean("devuelto")));
                }

            } catch (SQLException e) {
                System.err.println("Error al listar préstamos: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar préstamos: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Obtiene la lista de préstamos pertenecientes a un estudiante específico.
     *
     * @param idEstudiante ID del estudiante.
     * @return Lista de objetos Prestamo asociados.
     */
    @Override
    public List<Prestamo> listarPorEstudiante(int idEstudiante) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_estudiante = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, idEstudiante);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Prestamo(rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"), rs.getDate("fecha_prestamo").toLocalDate(), rs.getDate("fecha_devolucion").toLocalDate(), rs.getBoolean("devuelto")));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al listar préstamos por estudiante: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar préstamos por estudiante: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Obtiene únicamente la lista de préstamos que se encuentran activos (no devueltos).
     *
     * @return Lista de objetos Prestamo vigentes.
     */
    @Override
    public List<Prestamo> listarActivos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE devuelto = 0";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Prestamo(rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"), rs.getDate("fecha_prestamo").toLocalDate(), rs.getDate("fecha_devolucion").toLocalDate(), rs.getBoolean("devuelto")));
                }

            } catch (SQLException e) {
                System.err.println("Error al listar préstamos activos: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar préstamos activos: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Actualiza la información de un préstamo existente (útil para marcar devoluciones).
     *
     * @param prestamo Objeto Prestamo con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    @Override
    public boolean actualizar(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET id_estudiante = ?, id_libro = ?, fecha_prestamo = ?, fecha_devolucion = ?, devuelto = ? WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, prestamo.getIdEstudiante());
                ps.setInt(2, prestamo.getIdLibro());
                ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
                ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
                ps.setBoolean(5, prestamo.isDevuelto());
                ps.setInt(6, prestamo.getId());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al actualizar préstamo: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al actualizar préstamo: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina un préstamo de la base de datos según su ID.
     *
     * @param id ID del préstamo a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al eliminar préstamo: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }
}
