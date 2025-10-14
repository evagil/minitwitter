import unrn.repository.ConexionMySQL;
import java.sql.Connection;
import java.sql.SQLException;
        // Prueba de conexión a la base de datos
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión a la base de datos exitosa!");
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
package unrn.main;

import unrn.controller.TweetController;
import unrn.services.TweetService;
import unrn.repository.TweetRepository;
import unrn.model.User;
import unrn.model.Tweet;

public class Main {
    public static void main(String[] args) {
        // Prueba de conexión a la base de datos
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión a la base de datos exitosa!");
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
