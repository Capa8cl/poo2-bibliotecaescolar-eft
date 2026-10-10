package cl.bibliotecaescolar.dao.impl;

import cl.bibliotecaescolar.config.ConexionDB;
import cl.bibliotecaescolar.dao.LibroDAO;
import cl.bibliotecaescolar.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implementación de la interface LibroDAO para la gestión de persistencia en MySQL.
 */
public class LibroDAOImpl implements LibroDAO {

    /**
     *
     * Guarda un nuevo libro en la base de datos.
     *
     * @param libro Objeto Libro a registrar.
     * @return true si fue guardado exitosamente, false en caso contrario.
     */
    @Override
    public boolean guardar(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getAutor());
                ps.setString(3, libro.getIsbn());
                ps.setString(4, libro.getEditorial());
                ps.setInt(5, libro.getStock());
                ps.setInt(6, libro.getIdCategoria());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al guardar libro: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al guardar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Obtiene un libro específico de la base de datos a partir de su ID.
     *
     * @param id ID del libro a buscar.
     * @return Objeto Libro si se encuentra, o null si no existe.
     */
    @Override
    public Libro obtenerPorId(int id) {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"), rs.getString("isbn"), rs.getString("editorial"), rs.getInt("stock"), rs.getInt("id_categoria"));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener libro por ID: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al obtener libro por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * Obtiene la lista completa de todos los libros registrados.
     *
     * @return Lista de objetos Libro.
     */
    @Override
    public List<Libro> listarTodos() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"), rs.getString("isbn"), rs.getString("editorial"), rs.getInt("stock"), rs.getInt("id_categoria")));
                }

            } catch (SQLException e) {
                System.err.println("Error al listar libros: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar libros: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Obtiene la lista de libros pertenecientes a una categoría específica.
     *
     * @param idCategoria ID de la categoría a consultar.
     * @return Lista de objetos Libro.
     */
    @Override
    public List<Libro> listarPorCategoria(int idCategoria) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE id_categoria = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, idCategoria);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"), rs.getString("isbn"), rs.getString("editorial"), rs.getInt("stock"), rs.getInt("id_categoria")));
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al listar libros por categoría: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al listar libros por categoría: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Actualiza la información de un libro existente.
     *
     * @param libro Objeto Libro con los datos actualizados.
     * @return true si fue actualizado, false en caso contrario.
     */
    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, editorial = ?, stock = ?, id_categoria = ? WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getAutor());
                ps.setString(3, libro.getIsbn());
                ps.setString(4, libro.getEditorial());
                ps.setInt(5, libro.getStock());
                ps.setInt(6, libro.getIdCategoria());
                ps.setInt(7, libro.getId());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al actualizar libro: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Actualiza el stock de un libro de manera sincronizada para evitar condiciones de carrera en hilos.
     *
     * @param idLibro  ID del libro.
     * @param cantidad Cantidad a ajustar (positiva o negativa).
     * @return true si el stock se actualizó correctamente.
     */
    @Override
    public synchronized boolean actualizarStock(int idLibro, int cantidad) {
        String sql = "UPDATE libros SET stock = stock + ? WHERE id = ? AND (stock + ?) >= 0";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, cantidad);
                ps.setInt(2, idLibro);
                ps.setInt(3, cantidad);

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al actualizar stock del libro: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al actualizar stock del libro: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina un libro de la base de datos según su ID.
     *
     * @param id ID del libro a eliminar.
     * @return true si fue eliminado, false en caso contrario.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";
        try {
            Connection conexion = ConexionDB.getInstance();

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Error al eliminar libro: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión al eliminar libro: " + e.getMessage());
            return false;
        }
    }
}
