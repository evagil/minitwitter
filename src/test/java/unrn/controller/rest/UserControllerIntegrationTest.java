package unrn.controller.rest;

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
import unrn.model.User;
import unrn.repository.TweetRepository;
import unrn.repository.UserRepository;
import unrn.services.UserService;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Limpiar tablas antes de cada test
        tweetRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("POST /usuarios con nombre válido retorna 200 y usuario creado")
    void crearUsuario_nombreValido_retorna200YUsuario() throws Exception {
        // Setup: preparar el escenario
        String nombreUsuario = "nuevoUsuario";

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/usuarios")
                        .param("userName", nombreUsuario)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value(nombreUsuario))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("POST /usuarios con nombre duplicado retorna 400")
    void crearUsuario_nombreDuplicado_retorna400() throws Exception {
        // Setup: preparar el escenario
        String nombreUsuario = "usuarioExistente";
        userService.crearUsuario(nombreUsuario);

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/usuarios")
                        .param("userName", nombreUsuario)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El nombre de usuario ya existe"));
    }

    @Test
    @DisplayName("POST /usuarios con nombre null retorna 400")
    void crearUsuario_nombreNull_retorna400() throws Exception {
        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(post("/usuarios")
                        .param("userName", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /usuarios retorna lista de usuarios")
    void listarUsuarios_retornaLista() throws Exception {
        // Setup: preparar el escenario
        userService.crearUsuario("usuarioUno");
        userService.crearUsuario("usuarioDos");
        userService.crearUsuario("usuarioTres");

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].userName").exists())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @DisplayName("GET /usuarios sin usuarios retorna lista vacía")
    void listarUsuarios_sinUsuarios_retornaListaVacia() throws Exception {
        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /usuarios/{id} con ID existente retorna 200 y usuario")
    void buscarUsuario_idExistente_retorna200YUsuario() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioParaBuscar");
        int id = usuario.getId();

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userName").value("usuarioParaBuscar"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} con ID inexistente retorna 404")
    void buscarUsuario_idInexistente_retorna404() throws Exception {
        // Setup: preparar el escenario
        int idInexistente = 999;

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(get("/usuarios/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} con ID existente retorna 200")
    void eliminarUsuario_idExistente_retorna200() throws Exception {
        // Setup: preparar el escenario
        User usuario = userService.crearUsuario("usuarioParaEliminar");
        int id = usuario.getId();

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado"));

        // Verificar que el usuario fue eliminado
        User usuarioEliminado = userService.buscarPorId(id);
        assertNull(usuarioEliminado, "El usuario debe haber sido eliminado");
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} con ID inexistente retorna 404")
    void eliminarUsuario_idInexistente_retorna404() throws Exception {
        // Setup: preparar el escenario
        int idInexistente = 999;

        // Ejercitación y Verificación: comprobar resultado esperado
        mockMvc.perform(delete("/usuarios/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }
}

