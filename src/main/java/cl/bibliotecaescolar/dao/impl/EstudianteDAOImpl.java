package cl.bibliotecaescolar.dao.impl;

import cl.bibliotecaescolar.config.ConexionDB;
import cl.bibliotecaescolar.dao.EstudianteDAO;
import cl.bibliotecaescolar.model.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implementación de la interface EstudianteDAO para la gestión de persistencia en MySQL.
 */
public class EstudianteDAOImpl implements EstudianteDAO {

    /**
     *
     * Guarda un nuevo estudiante en la base de datos.
     *
     * @param estudiante Objeto Estudiante a registrar.
     * @return true si fue guardado exitosamente, false en caso contrario.
     */
    @Override
    public boolean guardar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (nombre, rut, correo, curso) VALUES (?, ?, ?, ?)";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getEmail());
                ps.setString(4, estudiante.getCurso());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al guardar estudiante: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al guardar estudiante: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Obtiene un estudiante específico de la base de datos a partir de su ID.
     *
     * @param id ID del estudiante a buscar.
     * @return Objeto Estudiante si se encuentra, o null si no existe.
     */
    @Override
    public Estudiante obtenerPorId(int id) {
        String sql = "SELECT * FROM estudiantes WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("curso"));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener estudiante por ID: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al obtener estudiante por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Busca un estudiante en la base de datos a través de su RUT.
     *
     * @param rut RUT del estudiante a buscar.
     * @return Objeto Estudiante si se encuentra, o null si no existe.
     */
    @Override
    public Estudiante obtenerPorRut(String rut) {
        String sql = "SELECT * FROM estudiantes WHERE rut = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, rut);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("curso"));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener estudiante por RUT: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al obtener estudiante por RUT: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un estudiante específico en la base de datos por su dirección de correo electrónico.
     *
     * @param correo Correo electrónico a consultar.
     * @return Objeto Estudiante hallado o null si no existe.
     */
    @Override
    public Estudiante obtenerPorCorreo(String correo) {
        String sql = "SELECT * FROM estudiantes WHERE correo = ?";
        try (Connection conexion = ConexionDB.getInstance(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("curso"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estudiante por correo: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Obtiene la lista completa de todos los estudiantes registrados.
     *
     * @return Lista de objetos Estudiante.
     */
    @Override
    public List<Estudiante> listarTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("curso")));
                }

            } catch (SQLException e) {
                System.err.println("Error al listar estudiantes: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar estudiantes: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Actualiza la información de un estudiante existente.
     *
     * @param estudiante Objeto Estudiante con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    @Override
    public boolean actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET nombre = ?, rut = ?, correo = ?, curso = ? WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getEmail());
                ps.setString(4, estudiante.getCurso());
                ps.setInt(5, estudiante.getId());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al actualizar estudiante: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al actualizar estudiante: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina un estudiante de la base de datos según su ID.
     *
     * @param id ID del estudiante a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al eliminar estudiante: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }
}
