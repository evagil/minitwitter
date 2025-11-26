package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Crear usuario con nombre válido debe funcionar")
    void crearUsuario_nombreValido_funciona() {
        // Setup: preparar el escenario
        String nombreValido = "usuarioValido";

        // Ejercitación: ejecutar la acción
        User user = new User(nombreValido);

        // Verificación: comprobar resultado esperado
        assertNotNull(user, "El usuario no debe ser nulo");
        assertEquals(nombreValido, user.getUserName(), "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Crear usuario con nombre de longitud mínima válida debe funcionar")
    void crearUsuario_longitudMinimaValida_funciona() {
        // Setup: preparar el escenario
        String nombreMinimo = "abcde"; // 5 caracteres

        // Ejercitación: ejecutar la acción
        User user = new User(nombreMinimo);

        // Verificación: comprobar resultado esperado
        assertNotNull(user, "El usuario no debe ser nulo");
        assertEquals(nombreMinimo, user.getUserName(), "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Crear usuario con nombre de longitud máxima válida debe funcionar")
    void crearUsuario_longitudMaximaValida_funciona() {
        // Setup: preparar el escenario
        String nombreMaximo = "a".repeat(25); // 25 caracteres

        // Ejercitación: ejecutar la acción
        User user = new User(nombreMaximo);

        // Verificación: comprobar resultado esperado
        assertNotNull(user, "El usuario no debe ser nulo");
        assertEquals(nombreMaximo, user.getUserName(), "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Crear usuario con nombre null lanza excepción")
    void crearUsuario_nombreNull_lanzaExcepcion() {
        // Setup: preparar el escenario
        String nombreNull = null;

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new User(nombreNull);
        }, "Debe lanzar excepción con nombre null");

        assertEquals(User.ERROR_USERNAME_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Crear usuario con nombre vacío lanza excepción")
    void crearUsuario_nombreVacio_lanzaExcepcion() {
        // Setup: preparar el escenario
        String nombreVacio = "";

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new User(nombreVacio);
        }, "Debe lanzar excepción con nombre vacío");

        assertEquals(User.ERROR_USERNAME_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Crear usuario con nombre corto lanza excepción")
    void crearUsuario_nombreCorto_lanzaExcepcion() {
        // Setup: preparar el escenario
        String nombreCorto = "abcd"; // 4 caracteres, menor a 5

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new User(nombreCorto);
        }, "Debe lanzar excepción con nombre corto");

        assertEquals(User.ERROR_USERNAME_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Crear usuario con nombre largo lanza excepción")
    void crearUsuario_nombreLargo_lanzaExcepcion() {
        // Setup: preparar el escenario
        String nombreLargo = "a".repeat(26); // 26 caracteres, mayor a 25

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new User(nombreLargo);
        }, "Debe lanzar excepción con nombre largo");

        assertEquals(User.ERROR_USERNAME_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Crear tweet con texto válido debe funcionar")
    void crearTweet_textoValido_funciona() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String textoValido = "Hola mundo";

        // Ejercitación: ejecutar la acción
        Tweet tweet = user.crearTweet(textoValido);

        // Verificación: comprobar resultado esperado
        assertNotNull(tweet, "El tweet no debe ser nulo");
        assertEquals(textoValido, tweet.getTexto(), "El texto del tweet debe coincidir");
        assertEquals(user, tweet.getAutor(), "El autor del tweet debe ser el usuario");
        assertFalse(tweet.esRetweet(), "El tweet creado no debe ser retweet");
    }

    @Test
    @DisplayName("Crear tweet con texto de longitud mínima válida debe funcionar")
    void crearTweet_longitudMinimaValida_funciona() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String textoMinimo = "a"; // 1 carácter

        // Ejercitación: ejecutar la acción
        Tweet tweet = user.crearTweet(textoMinimo);

        // Verificación: comprobar resultado esperado
        assertNotNull(tweet, "El tweet no debe ser nulo");
        assertEquals(textoMinimo, tweet.getTexto(), "El texto del tweet debe coincidir");
    }

    @Test
    @DisplayName("Crear tweet con texto de longitud máxima válida debe funcionar")
    void crearTweet_longitudMaximaValida_funciona() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String textoMaximo = "a".repeat(280); // 280 caracteres

        // Ejercitación: ejecutar la acción
        Tweet tweet = user.crearTweet(textoMaximo);

        // Verificación: comprobar resultado esperado
        assertNotNull(tweet, "El tweet no debe ser nulo");
        assertEquals(textoMaximo, tweet.getTexto(), "El texto del tweet debe coincidir");
    }

    @Test
    @DisplayName("Crear tweet con texto null lanza excepción")
    void crearTweet_textoNull_lanzaExcepcion() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String textoNull = null;

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            user.crearTweet(textoNull);
        }, "Debe lanzar excepción con texto null");

        assertEquals(User.ERROR_TWEET_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Crear tweet con texto vacío lanza excepción")
    void crearTweet_textoVacio_lanzaExcepcion() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String textoVacio = "";

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            user.crearTweet(textoVacio);
        }, "Debe lanzar excepción con texto vacío");

        assertEquals(User.ERROR_TWEET_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Crear tweet con texto largo lanza excepción")
    void crearTweet_textoLargo_lanzaExcepcion() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String textoLargo = "a".repeat(281); // 281 caracteres, mayor a 280

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            user.crearTweet(textoLargo);
        }, "Debe lanzar excepción con texto largo");

        assertEquals(User.ERROR_TWEET_LONGITUD, ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("No se puede hacer retweet de propio tweet")
    void hacerRetweet_propioTweet_lanzaExcepcion() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        Tweet tweet = user.crearTweet("Hola mundo");

        // Ejercitación y Verificación: comprobar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            user.hacerRetweet(tweet);
        }, "Debe lanzar excepción al intentar retwittear propio tweet");

        assertEquals(User.ERROR_RETWEET_PROPIO, ex.getMessage(), 
            "El mensaje de error debe indicar que no se puede retwittear propio tweet");
    }

    @Test
    @DisplayName("Hacer retweet de tweet de otro usuario debe funcionar")
    void hacerRetweet_tweetOtroUsuario_funciona() {
        // Setup: preparar el escenario
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");
        Tweet tweetOriginal = user1.crearTweet("Texto original");

        // Ejercitación: ejecutar la acción
        Tweet retweet = user2.hacerRetweet(tweetOriginal);

        // Verificación: comprobar resultado esperado
        assertNotNull(retweet, "El retweet no debe ser nulo");
        assertTrue(retweet.esRetweet(), "El retweet debe ser retweet");
        assertEquals(tweetOriginal, retweet.tweetDeOrigen(), 
            "El retweet debe conocer su tweet original");
        assertEquals(user2, retweet.getAutor(), 
            "El autor del retweet debe ser el usuario que lo hizo");
        assertEquals(tweetOriginal.getTexto(), retweet.getTexto(), 
            "El texto del retweet debe ser el del tweet original");
    }

    @Test
    @DisplayName("Usuario puede crear múltiples tweets")
    void crearMultiplesTweets_funciona() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");

        // Ejercitación: ejecutar la acción
        Tweet tweet1 = user.crearTweet("Primer tweet");
        Tweet tweet2 = user.crearTweet("Segundo tweet");
        Tweet tweet3 = user.crearTweet("Tercer tweet");

        // Verificación: comprobar resultado esperado
        assertEquals(3, user.getTweets().size(), 
            "El usuario debe tener 3 tweets");
        assertTrue(user.getTweets().contains(tweet1), 
            "El usuario debe contener el primer tweet");
        assertTrue(user.getTweets().contains(tweet2), 
            "El usuario debe contener el segundo tweet");
        assertTrue(user.getTweets().contains(tweet3), 
            "El usuario debe contener el tercer tweet");
    }

    @Test
    @DisplayName("Eliminar tweets limpia la lista de tweets del usuario")
    void eliminarTweets_limpiaListaTweets() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        user.crearTweet("Primer tweet");
        user.crearTweet("Segundo tweet");
        assertEquals(2, user.getTweets().size(), "Debe haber 2 tweets");

        // Ejercitación: ejecutar la acción
        user.eliminarTweets();

        // Verificación: comprobar resultado esperado
        assertTrue(user.getTweets().isEmpty(), "La lista de tweets debe estar vacía");
    }
}
