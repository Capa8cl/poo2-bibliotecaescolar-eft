package cl.bibliotecaescolar.dao.impl;

import cl.bibliotecaescolar.config.ConexionDB;
import cl.bibliotecaescolar.dao.UsuarioDAO;
import cl.bibliotecaescolar.model.Usuario;
import cl.bibliotecaescolar.model.RolUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implementación de la interface UsuarioDAO para la gestión de persistencia en MySQL.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    /**
     *
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto Usuario a registrar.
     * @return true si fue guardado exitosamente, false en caso contrario.
     */
    @Override
    public boolean guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, rut, correo, contraseña, rol) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {

                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getRut());
                ps.setString(3, usuario.getEmail());
                ps.setString(4, usuario.getContrasenia());
                ps.setString(5, usuario.getRol().name().toLowerCase());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al guardar usuario: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Autentica a un usuario en el sistema verificando sus credenciales.
     *
     * @param correo      Correo electrónico del usuario.
     * @param contrasenia Contraseña del usuario.
     * @return El objeto Usuario si las credenciales coinciden exactamente, o null si son incorrectas.
     */
    @Override
    public Usuario autenticar(String correo, String contrasenia) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, correo);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String passBD = rs.getString("contraseña");

                        if (passBD != null && passBD.equals(contrasenia)) {
                            return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), passBD, RolUsuario.valueOf(rs.getString("rol").toUpperCase()));
                        }
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al autenticar usuario: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al autenticar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Obtiene un usuario específico de la base de datos a partir de su ID.
     *
     * @param id ID del usuario a buscar.
     * @return Objeto Usuario si se encuentra, o null si no existe.
     */
    @Override
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("contraseña"), RolUsuario.valueOf(rs.getString("rol").toUpperCase()));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al obtener usuario por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Obtiene la lista completa de todos los usuarios registrados.
     *
     * @return Lista de objetos Usuario.
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("contraseña"), RolUsuario.valueOf(rs.getString("rol").toUpperCase())));
                }

            } catch (SQLException e) {
                System.err.println("Error al listar usuarios: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Actualiza la información de un usuario existente.
     *
     * @param usuario Objeto Usuario con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, rut = ?, correo = ?, contraseña = ?, rol = ? WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getRut());
                ps.setString(3, usuario.getEmail());
                ps.setString(4, usuario.getContrasenia());
                ps.setString(5, usuario.getRol().name().toLowerCase());
                ps.setInt(6, usuario.getId());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al actualizar usuario: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina un usuario de la base de datos según su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al eliminar usuario: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
