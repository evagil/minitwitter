package unrn.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class ConexionMySQL {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try (InputStream input = ConexionMySQL.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                url = prop.getProperty("spring.datasource.url");
                username = prop.getProperty("spring.datasource.username");
                password = prop.getProperty("spring.datasource.password");
                driver = prop.getProperty("spring.datasource.driver-class-name");
                Class.forName(driver);
            } else {
                throw new RuntimeException("No se encontró application.properties");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error cargando configuración de base de datos", e);
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
