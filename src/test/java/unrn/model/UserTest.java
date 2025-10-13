package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Crear usuario con nombre válido debe funcionar")
    void crearUsuario_nombreValido_funciona() {
        User user = new User("usuarioValido");
        assertNotNull(user, "El usuario no debe ser nulo");
    }

    @Test
    @DisplayName("Crear usuario con nombre corto lanza excepción")
    void crearUsuario_nombreCorto_lanzaExcepcion() {
        var ex = assertThrows(RuntimeException.class, () -> new User("abc"));
        assertEquals(User.ERROR_USERNAME_LONGITUD, ex.getMessage());
    }

    @Test
    @DisplayName("Crear tweet con texto válido debe funcionar")
    void crearTweet_textoValido_funciona() {
        User user = new User("usuarioValido");
        Tweet tweet = user.crearTweet("Hola mundo");
        assertNotNull(tweet, "El tweet no debe ser nulo");
    }

    @Test
    @DisplayName("Crear tweet con texto largo lanza excepción")
    void crearTweet_textoLargo_lanzaExcepcion() {
        User user = new User("usuarioValido");
        String texto = "a".repeat(281);
        var ex = assertThrows(RuntimeException.class, () -> user.crearTweet(texto));
        assertEquals(User.ERROR_TWEET_LONGITUD, ex.getMessage());
    }

    @Test
    @DisplayName("No se puede hacer retweet de propio tweet")
    void hacerRetweet_propioTweet_lanzaExcepcion() {
        User user = new User("usuarioValido");
        Tweet tweet = user.crearTweet("Hola mundo");
        var ex = assertThrows(RuntimeException.class, () -> user.hacerRetweet(tweet));
        assertEquals(User.ERROR_RETWEET_PROPIO, ex.getMessage());
    }
}