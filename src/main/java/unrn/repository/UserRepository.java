package unrn.repository;

import unrn.model.User;
import java.sql.*;

public class UserRepository {

    public void guardar(User user) {
        String sql = "INSERT INTO users (username) VALUES (?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUserName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    public User buscarPorId(int id) {
        String sql = "SELECT id, username FROM users WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("username"));
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por id", e);
        }
        return null;
    }

    public User buscarPorUserName(String userName) {
        String sql = "SELECT id, username FROM users WHERE username = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("username"));
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username", e);
        }
        return null;
    }

}
