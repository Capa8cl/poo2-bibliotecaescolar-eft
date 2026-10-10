package cl.bibliotecaescolar.dao.impl;

import cl.bibliotecaescolar.config.ConexionDB;
import cl.bibliotecaescolar.dao.CategoriaDAO;
import cl.bibliotecaescolar.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implementación de la interface CategoriaDAO para la gestión de persistencia en MySQL.
 */
public class CategoriaDAOImpl implements CategoriaDAO {

    /**
     *
     * Guarda una nueva categoría en la base de datos.
     *
     * @param categoria Objeto Categoría a registrar.
     * @return Devuelve true si fue guardada y false si no fue guardada.
     */
    @Override
    public boolean guardar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, categoria.getNombre());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al guardar categoría: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al guardar categoría: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Obtiene una categoría específica de la base de datos a partir de su ID.
     *
     * @param id ID de la categoría a buscar.
     * @return Objeto Categoría si se encuentra, o null si no existe.
     */
    @Override
    public Categoria obtenerPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Categoria(rs.getInt("id"), rs.getString("nombre"));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener categoría por ID: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al obtener categoría por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Obtiene la lista completa de todas las categorías registradas en la base de datos.
     *
     * @return Lista de objetos Categoría. Si no hay registros, retorna una lista vacía.
     */
    @Override
    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
                }

            } catch (SQLException e) {
                System.err.println("Error al listar categorías: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Actualiza la información de una categoría existente en la base de datos.
     *
     * @param categoria Objeto Categoría con los datos actualizados.
     * @return Devuelve true si la categoría fue actualizada o false en caso contrario.
     */
    @Override
    public boolean actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, categoria.getNombre());
                ps.setInt(2, categoria.getId());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al actualizar categoría: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina una categoría de la base de datos según su ID.
     *
     * @param id Identificador único de la categoría a eliminar.
     * @return Devuelve true si la categoría fue eliminada o false en caso contrario.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al eliminar categoría: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
}
