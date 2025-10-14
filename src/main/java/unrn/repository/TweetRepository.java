package unrn.repository;

import unrn.model.Tweet;
import unrn.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TweetRepository {

    public void guardar(Tweet tweet) {
        String sql = "INSERT INTO tweets (user_id, texto, tweet_original_id) VALUES (?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tweet.getAutorId());
            ps.setString(2, tweet.getTexto());
            if (tweet.esRetweet() && tweet.tweetDeOrigen() != null) {
                ps.setInt(3, tweet.tweetDeOrigen().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar tweet", e);
        }
    }

    public List<Tweet> tweetsDeUsuario(User user) {
        List<Tweet> resultado = new ArrayList<>();
        String sql = "SELECT id, texto, tweet_original_id FROM tweets WHERE user_id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Aquí deberías reconstruir el Tweet con los datos de la base
                    // Se asume que tienes un constructor adecuado o setters
                    // Ejemplo:
                    // Tweet tweet = new Tweet(user, rs.getString("texto"));
                    // resultado.add(tweet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener tweets de usuario", e);
        }
        return resultado;
    }
}
