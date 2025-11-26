package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetTest {

    @Test
    @DisplayName("Tweet creado no es retweet")
    void tweetCreado_noEsRetweet() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String texto = "Texto del tweet";

        // Ejercitación: ejecutar la acción
        Tweet tweet = user.crearTweet(texto);

        // Verificación: comprobar resultado esperado
        assertFalse(tweet.esRetweet(), "El tweet creado no debe ser retweet");
        assertNull(tweet.getTweetOriginal(), "El tweet original debe ser null");
        assertNull(tweet.tweetDeOrigen(), "El tweet de origen debe ser null");
    }

    @Test
    @DisplayName("Retweet conoce su tweet original")
    void retweet_conoceTweetOriginal() {
        // Setup: preparar el escenario
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");
        Tweet tweetOriginal = user1.crearTweet("Texto original");

        // Ejercitación: ejecutar la acción
        Tweet retweet = user2.hacerRetweet(tweetOriginal);

        // Verificación: comprobar resultado esperado
        assertTrue(retweet.esRetweet(), "El retweet debe ser retweet");
        assertEquals(tweetOriginal, retweet.tweetDeOrigen(), 
            "El retweet debe conocer su tweet original");
        assertEquals(tweetOriginal, retweet.getTweetOriginal(), 
            "El retweet debe tener referencia al tweet original");
    }

    @Test
    @DisplayName("Retweet tiene el mismo texto que el tweet original")
    void retweet_tieneMismoTextoQueOriginal() {
        // Setup: preparar el escenario
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");
        String textoOriginal = "Texto original del tweet";
        Tweet tweetOriginal = user1.crearTweet(textoOriginal);

        // Ejercitación: ejecutar la acción
        Tweet retweet = user2.hacerRetweet(tweetOriginal);

        // Verificación: comprobar resultado esperado
        assertEquals(textoOriginal, retweet.getTexto(), 
            "El retweet debe tener el mismo texto que el original");
        assertEquals(tweetOriginal.getTexto(), retweet.getTexto(), 
            "El texto del retweet debe coincidir con el del original");
    }

    @Test
    @DisplayName("Retweet tiene autor diferente al tweet original")
    void retweet_tieneAutorDiferente() {
        // Setup: preparar el escenario
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");
        Tweet tweetOriginal = user1.crearTweet("Texto original");

        // Ejercitación: ejecutar la acción
        Tweet retweet = user2.hacerRetweet(tweetOriginal);

        // Verificación: comprobar resultado esperado
        assertEquals(user2, retweet.getAutor(), 
            "El autor del retweet debe ser el usuario que lo hizo");
        assertEquals(user1, tweetOriginal.getAutor(), 
            "El autor del tweet original debe ser el usuario original");
        assertNotEquals(retweet.getAutor(), tweetOriginal.getAutor(), 
            "El autor del retweet debe ser diferente al del original");
    }

    @Test
    @DisplayName("esAutor retorna true cuando el usuario es el autor")
    void esAutor_usuarioEsAutor_retornaTrue() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        Tweet tweet = user.crearTweet("Texto");

        // Ejercitación: ejecutar la acción
        boolean resultado = tweet.esAutor(user);

        // Verificación: comprobar resultado esperado
        assertTrue(resultado, "esAutor debe retornar true cuando el usuario es el autor");
    }

    @Test
    @DisplayName("esAutor retorna false cuando el usuario no es el autor")
    void esAutor_usuarioNoEsAutor_retornaFalse() {
        // Setup: preparar el escenario
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");
        Tweet tweet = user1.crearTweet("Texto");

        // Ejercitación: ejecutar la acción
        boolean resultado = tweet.esAutor(user2);

        // Verificación: comprobar resultado esperado
        assertFalse(resultado, "esAutor debe retornar false cuando el usuario no es el autor");
    }

    @Test
    @DisplayName("esAutor retorna false cuando el usuario es null")
    void esAutor_usuarioNull_retornaFalse() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        Tweet tweet = user.crearTweet("Texto");

        // Ejercitación: ejecutar la acción
        boolean resultado = tweet.esAutor(null);

        // Verificación: comprobar resultado esperado
        assertFalse(resultado, "esAutor debe retornar false cuando el usuario es null");
    }

    @Test
    @DisplayName("esAutor retorna false cuando el autor del tweet es null")
    void esAutor_autorNull_retornaFalse() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        Tweet tweet = new Tweet(null, "Texto");

        // Ejercitación: ejecutar la acción
        boolean resultado = tweet.esAutor(user);

        // Verificación: comprobar resultado esperado
        assertFalse(resultado, "esAutor debe retornar false cuando el autor es null");
    }

    @Test
    @DisplayName("Tweet.retweet con tweet original null lanza excepción")
    void retweet_tweetOriginalNull_lanzaExcepcion() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        Tweet tweetOriginalNull = null;

        // Ejercitación y Verificación: comprobar que lanza excepción
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Tweet.retweet(user, tweetOriginalNull);
        }, "Debe lanzar excepción cuando el tweet original es null");

        assertEquals("El tweet original no puede ser nulo", ex.getMessage(), 
            "El mensaje de error debe indicar que el tweet original no puede ser nulo");
    }

    @Test
    @DisplayName("Tweet creado con constructor tiene autor y texto correctos")
    void tweet_constructor_autorYTextoCorrectos() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        String texto = "Texto del tweet";

        // Ejercitación: ejecutar la acción
        Tweet tweet = new Tweet(user, texto);

        // Verificación: comprobar resultado esperado
        assertEquals(user, tweet.getAutor(), "El autor del tweet debe coincidir");
        assertEquals(texto, tweet.getTexto(), "El texto del tweet debe coincidir");
        assertNull(tweet.getTweetOriginal(), "El tweet original debe ser null");
    }

    @Test
    @DisplayName("Tweet creado con constructor de 4 parámetros tiene todos los campos correctos")
    void tweet_constructor4Parametros_todosLosCamposCorrectos() {
        // Setup: preparar el escenario
        User autor = new User("usuarioValido");
        autor.setId(1);
        String texto = "Texto del tweet";
        Tweet tweetOriginal = new Tweet(autor, "Tweet original");
        tweetOriginal.setId(100);
        int id = 200;

        // Ejercitación: ejecutar la acción
        Tweet tweet = new Tweet(id, autor, texto, tweetOriginal);

        // Verificación: comprobar resultado esperado
        assertEquals(id, tweet.getId(), "El ID debe coincidir");
        assertEquals(autor, tweet.getAutor(), "El autor debe coincidir");
        assertEquals(texto, tweet.getTexto(), "El texto debe coincidir");
        assertEquals(tweetOriginal, tweet.getTweetOriginal(), "El tweet original debe coincidir");
    }

    @Test
    @DisplayName("esAutor retorna true cuando ambos usuarios tienen mismo ID")
    void esAutor_mismoId_retornaTrue() {
        // Setup: preparar el escenario
        User user1 = new User("usuarioUno");
        user1.setId(1);
        User user2 = new User("usuarioDos");
        user2.setId(1); // Mismo ID
        Tweet tweet = new Tweet(user1, "Texto");

        // Ejercitación: ejecutar la acción
        boolean resultado = tweet.esAutor(user2);

        // Verificación: comprobar resultado esperado
        assertTrue(resultado, "esAutor debe retornar true cuando los IDs coinciden");
    }

    @Test
    @DisplayName("esAutor retorna true cuando ambos usuarios son la misma instancia sin ID")
    void esAutor_mismaInstanciaSinId_retornaTrue() {
        // Setup: preparar el escenario
        User user = new User("usuarioValido");
        // user no tiene ID asignado
        Tweet tweet = new Tweet(user, "Texto");

        // Ejercitación: ejecutar la acción
        boolean resultado = tweet.esAutor(user);

        // Verificación: comprobar resultado esperado
        assertTrue(resultado, "esAutor debe retornar true cuando es la misma instancia sin ID");
    }
}
