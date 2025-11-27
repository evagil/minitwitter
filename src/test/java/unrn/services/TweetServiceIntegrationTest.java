package unrn.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.repository.TweetRepository;
import unrn.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TweetServiceIntegrationTest {

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Limpia las tablas antes de cada test
        tweetRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Crear tweet con usuario y texto válidos debe persistirse correctamente")
    void crearTweet_usuarioYTextoValidos_persisteCorrectamente() {
       
        User usuario = userService.crearUsuario("usuarioParaTweet");
        String texto = "Este es mi primer tweet";

        Tweet tweetCreado = tweetService.crearTweet(usuario, texto);

        assertNotNull(tweetCreado, "El tweet creado no debe ser nulo");
        assertNotNull(tweetCreado.getId(), "El tweet debe tener un ID asignado");
        assertEquals(texto, tweetCreado.getTexto(), "El texto del tweet debe coincidir");
        assertEquals(usuario.getId(), tweetCreado.getAutor().getId(), 
            "El autor del tweet debe coincidir");
        assertFalse(tweetCreado.esRetweet(), "El tweet no debe ser retweet");

        // Verifica la persistencia en BD
        Tweet tweetEnBD = tweetRepository.findById(tweetCreado.getId()).orElse(null);
        assertNotNull(tweetEnBD, "El tweet debe existir en la base de datos");
        assertEquals(texto, tweetEnBD.getTexto(), "El texto en BD debe coincidir");
    }

    @Test
    @DisplayName("Crear tweet con usuario inexistente lanza excepción")
    void crearTweet_usuarioInexistente_lanzaExcepcion() {
 
        User usuarioInexistente = new User("usuarioInexistente");
        usuarioInexistente.setId(999);
        String texto = "Texto del tweet";

        //Verifica: comprueba que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tweetService.crearTweet(usuarioInexistente, texto);
        }, "Debe lanzar excepción con usuario inexistente");

        assertEquals("Usuario no encontrado", ex.getMessage(), 
            "El mensaje de error debe indicar que el usuario no fue encontrado");
    }

    @Test
    @DisplayName("Hacer retweet con usuario y tweet válidos debe persistirse correctamente")
    void hacerRetweet_usuarioYTweetValidos_persisteCorrectamente() {
        // Setup: preparar el escenario
        User autorOriginal = userService.crearUsuario("autorOriginal");
        User usuarioRetweeter = userService.crearUsuario("usuarioRetweeter");
        Tweet tweetOriginal = tweetService.crearTweet(autorOriginal, "Tweet original");

        Tweet retweet = tweetService.hacerRetweet(usuarioRetweeter, tweetOriginal);

        assertNotNull(retweet, "El retweet creado no debe ser nulo");
        assertNotNull(retweet.getId(), "El retweet debe tener un ID asignado");
        assertTrue(retweet.esRetweet(), "El retweet debe ser retweet");
        assertEquals(tweetOriginal.getTexto(), retweet.getTexto(), 
            "El texto del retweet debe ser el del original");
        assertEquals(usuarioRetweeter.getId(), retweet.getAutor().getId(), 
            "El autor del retweet debe ser el usuario que lo hizo");
        assertEquals(tweetOriginal.getId(), retweet.getTweetOriginal().getId(), 
            "El tweet original debe coincidir");

        // Verifica la persistencia en BD
        Tweet retweetEnBD = tweetRepository.findById(retweet.getId()).orElse(null);
        assertNotNull(retweetEnBD, "El retweet debe existir en la base de datos");
        assertTrue(retweetEnBD.esRetweet(), "El retweet en BD debe ser retweet");
    }

    @Test
    @DisplayName("Hacer retweet de propio tweet lanza excepción")
    void hacerRetweet_propioTweet_lanzaExcepcion() {
      
        User usuario = userService.crearUsuario("usuarioPropio");
        Tweet tweetOriginal = tweetService.crearTweet(usuario, "Mi propio tweet");

        //Verifica: comprueba que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tweetService.hacerRetweet(usuario, tweetOriginal);
        }, "Debe lanzar excepción al intentar retwittear propio tweet");

        assertEquals("No puedes hacer retweet de tu propio tweet", ex.getMessage(), 
            "El mensaje de error debe indicar que no se puede retwittear propio tweet");
    }

    @Test
    @DisplayName("Hacer retweet con usuario inexistente lanza excepción")
    void hacerRetweet_usuarioInexistente_lanzaExcepcion() {
     
        User autorOriginal = userService.crearUsuario("autorOriginal");
        Tweet tweetOriginal = tweetService.crearTweet(autorOriginal, "Tweet original");
        User usuarioInexistente = new User("usuarioInexistente");
        usuarioInexistente.setId(999);

        //Verifica: comprueba que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tweetService.hacerRetweet(usuarioInexistente, tweetOriginal);
        }, "Debe lanzar excepción con usuario inexistente");

        assertEquals("Usuario no encontrado", ex.getMessage(), 
            "El mensaje de error debe indicar que el usuario no fue encontrado");
    }

    @Test
    @DisplayName("Hacer retweet con tweet inexistente lanza excepción")
    void hacerRetweet_tweetInexistente_lanzaExcepcion() {
      
        User usuario = userService.crearUsuario("usuarioRetweeter");
        Tweet tweetInexistente = new Tweet(usuario, "Tweet inexistente");
        tweetInexistente.setId(999);

        //Verifica: comprueba que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tweetService.hacerRetweet(usuario, tweetInexistente);
        }, "Debe lanzar excepción con tweet inexistente");

        assertEquals("Tweet original no encontrado", ex.getMessage(), 
            "El mensaje de error debe indicar que el tweet original no fue encontrado");
    }

    @Test
    @DisplayName("Obtener tweets de usuario retorna lista de tweets del usuario")
    void obtenerTweetsDeUsuario_retornaListaTweets() {
        
        User usuario = userService.crearUsuario("usuarioConTweets");
        tweetService.crearTweet(usuario, "Primer tweet");
        tweetService.crearTweet(usuario, "Segundo tweet");
        tweetService.crearTweet(usuario, "Tercer tweet");

        List<Tweet> tweets = tweetService.obtenerTweetsDeUsuario(usuario);

        assertEquals(3, tweets.size(), "Debe haber 3 tweets del usuario");
        assertTrue(tweets.stream().allMatch(t -> t.getAutor().getId().equals(usuario.getId())), 
            "Todos los tweets deben ser del usuario");
    }

    @Test
    @DisplayName("Obtener tweets de usuario sin tweets retorna lista vacía")
    void obtenerTweetsDeUsuario_sinTweets_retornaListaVacia() {
     
        User usuario = userService.crearUsuario("usuarioSinTweets");

        List<Tweet> tweets = tweetService.obtenerTweetsDeUsuario(usuario);

        assertNotNull(tweets, "La lista no debe ser nula");
        assertTrue(tweets.isEmpty(), "La lista debe estar vacía");
    }

    @Test
    @DisplayName("Obtener timeline retorna solo tweets originales sin retweets")
    void obtenerTimeline_retornaSoloTweetsOriginales() {
     
        User usuario1 = userService.crearUsuario("usuarioUno");
        User usuario2 = userService.crearUsuario("usuarioDos");
        tweetService.crearTweet(usuario1, "Tweet original 1");
        Tweet tweet2 = tweetService.crearTweet(usuario2, "Tweet original 2");
        tweetService.hacerRetweet(usuario1, tweet2); // Retweet que no debe aparecer

      
        List<Tweet> timeline = tweetService.obtenerTodosLosTweets(0, 10);

        assertEquals(2, timeline.size(), "El timeline debe tener 2 tweets originales");
        assertTrue(timeline.stream().noneMatch(Tweet::esRetweet), 
            "El timeline no debe contener retweets");
    }

    @Test
    @DisplayName("Obtener timeline con paginación retorna cantidad correcta")
    void obtenerTimeline_conPaginacion_retornaCantidadCorrecta() {
    
        User usuario = userService.crearUsuario("usuarioConMuchosTweets");
        for (int i = 1; i <= 15; i++) {
            tweetService.crearTweet(usuario, "Tweet " + i);
        }

        List<Tweet> primeraPagina = tweetService.obtenerTodosLosTweets(0, 10);
        List<Tweet> segundaPagina = tweetService.obtenerTodosLosTweets(10, 10);

        assertEquals(10, primeraPagina.size(), "La primera página debe tener 10 tweets");
        assertEquals(5, segundaPagina.size(), "La segunda página debe tener 5 tweets");
    }

    @Test
    @DisplayName("Obtener tweets de usuario con paginación retorna cantidad correcta")
    void obtenerTweetsDeUsuarioConPaginacion_retornaCantidadCorrecta() {
      
        User usuario = userService.crearUsuario("usuarioConMuchosTweets");
        for (int i = 1; i <= 20; i++) {
            tweetService.crearTweet(usuario, "Tweet " + i);
        }

        List<Tweet> primeraPagina = tweetService.obtenerTweetsDeUsuarioConPaginacion(usuario, 0, 15);
        List<Tweet> segundaPagina = tweetService.obtenerTweetsDeUsuarioConPaginacion(usuario, 15, 15);

        assertEquals(15, primeraPagina.size(), "La primera página debe tener 15 tweets");
        assertEquals(5, segundaPagina.size(), "La segunda página debe tener 5 tweets");
    }

    @Test
    @DisplayName("Buscar tweet por ID existente retorna el tweet")
    void buscarTweetPorId_tweetExistente_retornaTweet() {
      
        User usuario = userService.crearUsuario("usuarioParaTweet");
        Tweet tweetCreado = tweetService.crearTweet(usuario, "Tweet para buscar");

        Tweet tweetEncontrado = tweetService.buscarTweetPorId(tweetCreado.getId());

        assertNotNull(tweetEncontrado, "El tweet encontrado no debe ser nulo");
        assertEquals(tweetCreado.getId(), tweetEncontrado.getId(), 
            "El ID del tweet debe coincidir");
        assertEquals("Tweet para buscar", tweetEncontrado.getTexto(), 
            "El texto del tweet debe coincidir");
    }

    @Test
    @DisplayName("Buscar tweet por ID inexistente retorna null")
    void buscarTweetPorId_tweetInexistente_retornaNull() {
      
        int idInexistente = 999;

        Tweet tweetEncontrado = tweetService.buscarTweetPorId(idInexistente);

        assertNull(tweetEncontrado, "El tweet no debe existir");
    }

    @Test
    @DisplayName("Contar tweets retorna cantidad de tweets originales")
    void contarTweets_retornaCantidadTweetsOriginales() {
        
        User usuario1 = userService.crearUsuario("usuarioUno");
        User usuario2 = userService.crearUsuario("usuarioDos");
        tweetService.crearTweet(usuario1, "Tweet 1");
        tweetService.crearTweet(usuario1, "Tweet 2");
        Tweet tweetOriginal = tweetService.crearTweet(usuario2, "Tweet 3");
        tweetService.hacerRetweet(usuario1, tweetOriginal); // Retweet que no debe contarse

        int cantidad = tweetService.contarTweets();

        assertEquals(3, cantidad, "Debe haber 3 tweets originales");
    }


    @Test
    @DisplayName("Obtener timeline con offset y limit cero retorna lista vacía")
    void obtenerTimeline_offsetYLimitCero_retornaListaVacia() {
       
        User usuario = userService.crearUsuario("usuarioConTweets");
        tweetService.crearTweet(usuario, "Tweet 1");

        List<Tweet> timeline = tweetService.obtenerTodosLosTweets(0, 0);

        assertNotNull(timeline, "La lista no debe ser nula");
        // Con limit 0, debería retornar lista vacía
    }
}

