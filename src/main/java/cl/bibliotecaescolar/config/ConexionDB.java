package cl.bibliotecaescolar.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * Gestiona la conexión hacia la base de datos MySQL aplicando el patrón Singleton.
 */
public class ConexionDB {
    private static Connection instancia;

    // Reemplazar la URL con la ruta de la BBDD.
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/biblioteca";

    // Reemplazar "root" (usuario) y "Duoc123" (contraseña) por las credenciales del servicio local de MySQL.
    private static final String USER = "root";
    private static final String PASSWORD = "Duoc123";

    /**
     *
     * Constructor privado para evitar que se creen objetos con "new ConexionDB()".
     */
    private ConexionDB() {
    }

    /**
     *
     * Devuelve la única instancia de la conexión a la base de datos (Singleton).
     *
     * @return Instancia de Connection.
     * @throws SQLException Si ocurre un error de acceso.
     */
    public static Connection getInstance() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            instancia = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return instancia;
    }
}

