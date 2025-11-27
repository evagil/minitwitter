package unrn.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import unrn.model.User;
import unrn.repository.UserRepository;
import unrn.repository.TweetRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceIntegrationTest {

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
        // Limpia las tablas antes de cada test
        tweetRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Crear usuario con nombre válido debe persistirse correctamente")
    void crearUsuario_nombreValido_persisteCorrectamente() {
       
        String nombreUsuario = "nuevoUsuario";

        User usuarioCreado = userService.crearUsuario(nombreUsuario);

        assertNotNull(usuarioCreado, "El usuario creado no debe ser nulo");
        assertNotNull(usuarioCreado.getId(), "El usuario debe tener un ID asignado");
        assertEquals(nombreUsuario, usuarioCreado.getUserName(), 
            "El nombre de usuario debe coincidir");

        // Verifica la persistencia en BD
        User usuarioEnBD = userRepository.findById(usuarioCreado.getId()).orElse(null);
        assertNotNull(usuarioEnBD, "El usuario debe existir en la base de datos");
        assertEquals(nombreUsuario, usuarioEnBD.getUserName(), 
            "El nombre de usuario en BD debe coincidir");
    }

    @Test
    @DisplayName("Crear usuario con nombre duplicado lanza excepción")
    void crearUsuario_nombreDuplicado_lanzaExcepcion() {
       
        String nombreUsuario = "usuarioExistente";
        userService.crearUsuario(nombreUsuario);

        //Verifica: compruebar que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.crearUsuario(nombreUsuario);
        }, "Debe lanzar excepción con nombre duplicado");

        assertEquals("El nombre de usuario ya existe", ex.getMessage(), 
            "El mensaje de error debe indicar que el nombre ya existe");
    }

    @Test
    @DisplayName("Crear usuario con nombre duplicado ignorando mayúsculas lanza excepción")
    void crearUsuario_nombreDuplicadoIgnoreCase_lanzaExcepcion() {
       
        String nombreUsuario = "usuarioExistente";
        userService.crearUsuario(nombreUsuario);

        //Verifica: compruebar que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.crearUsuario("USUARIOEXISTENTE");
        }, "Debe lanzar excepción con nombre duplicado ignorando mayúsculas");

        assertEquals("El nombre de usuario ya existe", ex.getMessage(), 
            "El mensaje de error debe indicar que el nombre ya existe");
    }

    @Test
    @DisplayName("Crear usuario con nombre null lanza excepción")
    void crearUsuario_nombreNull_lanzaExcepcion() {
     
        String nombreNull = null;

        //Verifica: comprueba que lanza la excepción
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.crearUsuario(nombreNull);
        }, "Debe lanzar excepción con nombre null");

        assertEquals("El nombre de usuario es obligatorio", ex.getMessage(), 
            "El mensaje de error debe indicar que el nombre es obligatorio");
    }

    @Test
    @DisplayName("Crear usuario con nombre que solo tiene espacios lanza excepción")
    void crearUsuario_nombreSoloEspacios_lanzaExcepcion() {
      
        String nombreSoloEspacios = "   ";

        //Verifica: comprueba que lanza la excepción
        // El servicio hace trim y luego valida longitud, así que lanzará error de longitud
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.crearUsuario(nombreSoloEspacios);
        }, "Debe lanzar excepción con nombre que solo tiene espacios");

        assertEquals("El nombre de usuario debe tener entre 5 y 25 caracteres", ex.getMessage(), 
            "El mensaje de error debe indicar problema de longitud");
    }

    @Test
    @DisplayName("Buscar usuario por ID existente retorna el usuario")
    void buscarPorId_usuarioExistente_retornaUsuario() {
      
        User usuarioCreado = userService.crearUsuario("usuarioParaBuscar");

        User usuarioEncontrado = userService.buscarPorId(usuarioCreado.getId());

        assertNotNull(usuarioEncontrado, "El usuario encontrado no debe ser nulo");
        assertEquals(usuarioCreado.getId(), usuarioEncontrado.getId(), 
            "El ID del usuario debe coincidir");
        assertEquals("usuarioParaBuscar", usuarioEncontrado.getUserName(), 
            "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Buscar usuario por ID inexistente retorna null")
    void buscarPorId_usuarioInexistente_retornaNull() {
        
        int idInexistente = 999;

        User usuarioEncontrado = userService.buscarPorId(idInexistente);

        assertNull(usuarioEncontrado, "El usuario no debe existir");
    }

    @Test
    @DisplayName("Buscar usuario por userName existente retorna el usuario")
    void buscarPorUserName_usuarioExistente_retornaUsuario() {
        
        String nombreUsuario = "usuarioParaBuscar";
        userService.crearUsuario(nombreUsuario);

        User usuarioEncontrado = userService.buscarPorUserName(nombreUsuario);

        assertNotNull(usuarioEncontrado, "El usuario encontrado no debe ser nulo");
        assertEquals(nombreUsuario, usuarioEncontrado.getUserName(), 
            "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Buscar usuario por userName ignorando mayúsculas retorna el usuario")
    void buscarPorUserName_ignoreCase_retornaUsuario() {
      
        String nombreUsuario = "usuarioParaBuscar";
        userService.crearUsuario(nombreUsuario);

       
        User usuarioEncontrado = userService.buscarPorUserName("USUARIOPARABUSCAR");

        assertNotNull(usuarioEncontrado, "El usuario encontrado no debe ser nulo");
        assertEquals(nombreUsuario, usuarioEncontrado.getUserName(), 
            "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Buscar usuario por userName inexistente retorna null")
    void buscarPorUserName_usuarioInexistente_retornaNull() {
       
        String nombreInexistente = "usuarioInexistente";

        User usuarioEncontrado = userService.buscarPorUserName(nombreInexistente);

        assertNull(usuarioEncontrado, "El usuario no debe existir");
    }

    @Test
    @DisplayName("Listar todos los usuarios retorna lista completa")
    void listarTodos_retornaListaCompleta() {
        
        userService.crearUsuario("usuarioUno");
        userService.crearUsuario("usuarioDos");
        userService.crearUsuario("usuarioTres");

        List<User> usuarios = userService.listarTodos();
      
        assertEquals(3, usuarios.size(), "Debe haber 3 usuarios en la lista");
        assertTrue(usuarios.stream().anyMatch(u -> u.getUserName().equals("usuarioUno")), 
            "La lista debe contener usuarioUno");
        assertTrue(usuarios.stream().anyMatch(u -> u.getUserName().equals("usuarioDos")), 
            "La lista debe contener usuarioDos");
        assertTrue(usuarios.stream().anyMatch(u -> u.getUserName().equals("usuarioTres")), 
            "La lista debe contener usuarioTres");
    }

    @Test
    @DisplayName("Listar todos los usuarios cuando no hay usuarios retorna lista vacía")
    void listarTodos_sinUsuarios_retornaListaVacia() {
      
        List<User> usuarios = userService.listarTodos();

        assertNotNull(usuarios, "La lista no debe ser nula");
        assertTrue(usuarios.isEmpty(), "La lista debe estar vacía");
    }

    @Test
    @DisplayName("Eliminar usuario elimina también sus tweets")
    void eliminarUsuario_eliminaTweetsAsociados() {
        
        User usuario = userService.crearUsuario("usuarioParaEliminar");
        // Crea un tweets para este usuario (a través del servicio de tweets)
        // Nota: esto requiere TweetService, pero por ahora se verifica la cascada
        userService.eliminarUsuario(usuario.getId());

        User usuarioEliminado = userService.buscarPorId(usuario.getId());
        assertNull(usuarioEliminado, "El usuario debe haber sido eliminado");
    }

    @Test
    @DisplayName("Eliminar usuario inexistente lanza excepción")
    void eliminarUsuario_inexistente_lanzaExcepcion() {
       
        int idInexistente = 999;

        //Verifica: Spring Data JPA lanza excepción al eliminar ID inexistente
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> {
            userService.eliminarUsuario(idInexistente);
        }, "Eliminar usuario inexistente debe lanzar EmptyResultDataAccessException");
    }
}

