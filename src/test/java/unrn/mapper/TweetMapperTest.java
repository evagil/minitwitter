package unrn.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unrn.dto.TweetDto;
import unrn.model.Tweet;
import unrn.model.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TweetMapperTest {

    @Test
    @DisplayName("TweetMapper.toDto convierte Tweet sin retweet a TweetDto correctamente")
    void toDto_tweetSinRetweet_convierteCorrectamente() {
        
        User autor = new User("usuarioTest");
        autor.setId(1);
        Tweet tweet = new Tweet(autor, "Texto del tweet");
        tweet.setId(10);
        tweet.setCreatedAt(new Date());
       
        TweetDto dto = TweetMapper.toDto(tweet);
      
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertEquals(tweet.getId(), dto.getId(), "El ID debe coincidir");
        assertEquals(tweet.getTexto(), dto.getTexto(), "El texto debe coincidir");
        assertEquals(tweet.getCreatedAt(), dto.getCreatedAt(), "La fecha debe coincidir");
        assertEquals(autor.getId(), dto.getAutorId(), "El ID del autor debe coincidir");
        assertEquals(autor.getUserName(), dto.getAutorUserName(), "El nombre del autor debe coincidir");
        assertNull(dto.getTweetOriginalId(), "El tweet original debe ser null");
    }

    @Test
    @DisplayName("TweetMapper.toDto convierte Retweet a TweetDto correctamente")
    void toDto_retweet_convierteCorrectamente() {
        
        User autorOriginal = new User("autorOriginal");
        autorOriginal.setId(1);
        User autorRetweeter = new User("autorRetweeter");
        autorRetweeter.setId(2);
        
        Tweet tweetOriginal = new Tweet(autorOriginal, "Tweet original");
        tweetOriginal.setId(100);
        tweetOriginal.setCreatedAt(new Date());
        
        Tweet retweet = Tweet.retweet(autorRetweeter, tweetOriginal);
        retweet.setId(200);
        retweet.setCreatedAt(new Date());
        
        TweetDto dto = TweetMapper.toDto(retweet);
        
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertEquals(retweet.getId(), dto.getId(), "El ID del retweet debe coincidir");
        assertEquals(tweetOriginal.getId(), dto.getTweetOriginalId(), "El ID del tweet original debe coincidir");
        assertEquals(autorRetweeter.getId(), dto.getAutorId(), "El ID del autor del retweet debe coincidir");
        assertEquals(autorOriginal.getUserName(), dto.getTweetOriginalAutor(), "El nombre del autor original debe coincidir");
    }

    @Test
    @DisplayName("TweetMapper.toDto con Tweet null lanza NullPointerException")
    void toDto_tweetNull_lanzaExcepcion() {
       
        Tweet tweet = null;

        //Verifica: comprueba que lanza la excepción
        assertThrows(NullPointerException.class, () -> {
            TweetMapper.toDto(tweet);
        }, "Debe lanzar NullPointerException con Tweet null");
    }

    @Test
    @DisplayName("TweetMapper.toDto con autor null maneja correctamente")
    void toDto_autorNull_manejaCorrectamente() {
        
        Tweet tweet = new Tweet(null, "Texto sin autor");
        tweet.setId(1);
        tweet.setCreatedAt(new Date());
       
        TweetDto dto = TweetMapper.toDto(tweet);
        
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertNull(dto.getAutorId(), "El ID del autor debe ser null");
        assertNull(dto.getAutorUserName(), "El nombre del autor debe ser null");
    }

    @Test
    @DisplayName("TweetMapper.toDto con tweetOriginal sin autor maneja correctamente")
    void toDto_tweetOriginalSinAutor_manejaCorrectamente() {
       
        User autorRetweeter = new User("autorRetweeter");
        autorRetweeter.setId(2);
        
        Tweet tweetOriginal = new Tweet(null, "Tweet original sin autor");
        tweetOriginal.setId(100);
        tweetOriginal.setCreatedAt(new Date());
        
        Tweet retweet = Tweet.retweet(autorRetweeter, tweetOriginal);
        retweet.setId(200);
        retweet.setCreatedAt(new Date());
        
        TweetDto dto = TweetMapper.toDto(retweet);
        
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertEquals(tweetOriginal.getId(), dto.getTweetOriginalId(), "El ID del tweet original debe coincidir");
        assertNull(dto.getTweetOriginalAutor(), "El autor del tweet original debe ser null");
    }
}

