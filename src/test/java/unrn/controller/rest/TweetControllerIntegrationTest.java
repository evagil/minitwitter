package unrn.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import unrn.dto.CreateRetweetRequest;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.repository.TweetRepository;
import unrn.repository.UserRepository;
import unrn.services.TweetService;
import unrn.services.UserService;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TweetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Limpiar tablas antes de cada test
        tweetRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("POST /tweets/crear con usuario y texto válidos retorna 200 y tweet creado")
    void crearTweet_usuarioYTextoValidos_retorna200YTweet() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioParaTweet");
        String texto = "Este es mi primer tweet";

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/tweets/crear")
                        .param("userId", String.valueOf(usuario.getId()))
                        .param("texto", texto)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.texto").value(texto))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.autorId").value(usuario.getId()));
    }

    @Test
    @DisplayName("POST /tweets/crear con usuario inexistente retorna error")
    void crearTweet_usuarioInexistente_retornaError() throws Exception {
        // Setup: preparar el escenario
        int usuarioIdInexistente = 999;
        String texto = "Texto del tweet";

        // Ejercitación y Verificación: comprobar resultado esperado
        // El GlobalExceptionHandler convierte RuntimeException en 400
        mockMvc.perform(post("/tweets/crear")
                        .param("userId", String.valueOf(usuarioIdInexistente))
                        .param("texto", texto)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tweets/retweet con usuario y tweet válidos retorna 200 y retweet creado")
    void hacerRetweet_usuarioYTweetValidos_retorna200YRetweet() throws Exception {
        // Setup: preparar el escenario
        User autorOriginal = userService.crearUsuario("autorOriginal");
        User usuarioRetweeter = userService.crearUsuario("usuarioRetweeter");
        Tweet tweetOriginal = tweetService.crearTweet(autorOriginal, "Tweet original");

        CreateRetweetRequest request = new CreateRetweetRequest();
        request.setUserId(usuarioRetweeter.getId());
        request.setTweetId(tweetOriginal.getId());

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/tweets/retweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.autorId").value(usuarioRetweeter.getId()))
                .andExpect(jsonPath("$.tweetOriginalId").value(tweetOriginal.getId()));
    }

    @Test
    @DisplayName("POST /tweets/retweet con usuario inexistente retorna 400")
    void hacerRetweet_usuarioInexistente_retorna400() throws Exception {
        // Setup: preparar el escenario
        User autorOriginal = userService.crearUsuario("autorOriginal");
        Tweet tweetOriginal = tweetService.crearTweet(autorOriginal, "Tweet original");

        CreateRetweetRequest request = new CreateRetweetRequest();
        request.setUserId(999); // Usuario inexistente
        request.setTweetId(tweetOriginal.getId());

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/tweets/retweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario no encontrado"));
    }

    @Test
    @DisplayName("POST /tweets/retweet con tweet inexistente retorna 400")
    void hacerRetweet_tweetInexistente_retorna400() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioRetweeter");

        CreateRetweetRequest request = new CreateRetweetRequest();
        request.setUserId(usuario.getId());
        request.setTweetId(999); // Tweet inexistente

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/tweets/retweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tweet no encontrado"));
    }

    @Test
    @DisplayName("POST /tweets/retweet de propio tweet retorna error")
    void hacerRetweet_propioTweet_retornaError() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioPropio");
        Tweet tweetOriginal = tweetService.crearTweet(usuario, "Mi propio tweet");

        CreateRetweetRequest request = new CreateRetweetRequest();
        request.setUserId(usuario.getId());
        request.setTweetId(tweetOriginal.getId());

        // Ejercitación y Verificación: comprobar resultado esperado
        // El servicio lanza RuntimeException que el GlobalExceptionHandler convierte en 400
        mockMvc.perform(post("/tweets/retweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No puedes hacer retweet de tu propio tweet"));
    }

    @Test
    @DisplayName("GET /tweets/timeline retorna lista de tweets originales")
    void obtenerTimeline_retornaListaTweetsOriginales() throws Exception {
        // Setup: preparar el escenario
        User usuario1 = userService.crearUsuario("usuarioUno");
        User usuario2 = userService.crearUsuario("usuarioDos");
        tweetService.crearTweet(usuario1, "Tweet original 1");
        tweetService.crearTweet(usuario2, "Tweet original 2");
        Tweet tweetOriginal = tweetService.crearTweet(usuario1, "Tweet original 3");
        tweetService.hacerRetweet(usuario2, tweetOriginal); // Retweet que no debe aparecer

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/timeline")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].texto").exists())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @DisplayName("GET /tweets/timeline con paginación retorna cantidad correcta")
    void obtenerTimeline_conPaginacion_retornaCantidadCorrecta() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioConMuchosTweets");
        for (int i = 1; i <= 15; i++) {
            tweetService.crearTweet(usuario, "Tweet " + i);
        }

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/timeline")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    @DisplayName("GET /tweets/timeline sin tweets retorna lista vacía")
    void obtenerTimeline_sinTweets_retornaListaVacia() throws Exception {
        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/timeline")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /tweets/de-usuario/{userId} retorna lista de tweets del usuario")
    void obtenerTweetsDeUsuario_retornaListaTweets() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioConTweets");
        tweetService.crearTweet(usuario, "Primer tweet");
        tweetService.crearTweet(usuario, "Segundo tweet");
        tweetService.crearTweet(usuario, "Tercer tweet");

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/de-usuario/{userId}", usuario.getId())
                        .param("offset", "0")
                        .param("limit", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].autorId").value(usuario.getId()));
    }

    @Test
    @DisplayName("GET /tweets/de-usuario/{userId} sin tweets retorna lista vacía")
    void obtenerTweetsDeUsuario_sinTweets_retornaListaVacia() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioSinTweets");

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/de-usuario/{userId}", usuario.getId())
                        .param("offset", "0")
                        .param("limit", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /tweets/retweets retorna lista de retweets")
    void listarRetweets_retornaListaRetweets() throws Exception {
        // Setup: preparar el escenario
        User autorOriginal = userService.crearUsuario("autorOriginal");
        User usuarioRetweeter = userService.crearUsuario("usuarioRetweeter");
        Tweet tweetOriginal = tweetService.crearTweet(autorOriginal, "Tweet original");
        tweetService.hacerRetweet(usuarioRetweeter, tweetOriginal);

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/retweets")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].retweetId").exists())
                .andExpect(jsonPath("$[0].originalTweetId").value(tweetOriginal.getId()))
                .andExpect(jsonPath("$[0].retweeterId").value(usuarioRetweeter.getId()));
    }

    @Test
    @DisplayName("GET /tweets/retweets sin retweets retorna lista vacía")
    void listarRetweets_sinRetweets_retornaListaVacia() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioSinRetweets");
        tweetService.crearTweet(usuario, "Tweet original");

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/tweets/retweets")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

