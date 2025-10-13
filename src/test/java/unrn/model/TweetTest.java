package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetTest {

    @Test
    @DisplayName("Tweet creado no es retweet")
    void tweetCreado_noEsRetweet() {
        User user = new User("usuarioValido");
        Tweet tweet = user.crearTweet("Texto");
        assertFalse(tweet.esRetweet(), "El tweet creado no debe ser retweet");
    }

    @Test
    @DisplayName("Retweet conoce su tweet original")
    void retweet_conoceTweetOriginal() {
        User user1 = new User("usuarioUno");
        User user2 = new User("usuarioDos");
        Tweet tweetOriginal = user1.crearTweet("Texto original");
        Tweet retweet = user2.hacerRetweet(tweetOriginal);
        assertTrue(retweet.esRetweet(), "El retweet debe ser retweet");
        assertEquals(tweetOriginal, retweet.tweetDeOrigen(), "El retweet debe conocer su tweet original");
    }
}