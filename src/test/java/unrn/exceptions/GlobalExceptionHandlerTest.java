package unrn.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GlobalExceptionHandler maneja RuntimeException y retorna 400")
    void handleRuntimeException_retorna400() throws Exception {
        
        String nombreInvalido = "abc"; // Menor a 5 caracteres

        
        mockMvc.perform(post("/usuarios")
                        .param("userName", nombreInvalido)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El nombre de usuario debe tener entre 5 y 25 caracteres"));
    }

    @Test
    @DisplayName("GlobalExceptionHandler maneja NullPointerException como RuntimeException y retorna 400")
    void handleNullPointerException_retorna400() throws Exception {
        // Crea un tweet con usuario null causará NullPointerException
        // NullPointerException extiende de RuntimeException, así que será manejado por handleRuntime
        int usuarioIdInexistente = 999;
        String texto = "Texto del tweet";
        
        // El controlador no valida null, causará NullPointerException que será manejado como RuntimeException (400)
        mockMvc.perform(post("/tweets/crear")
                        .param("userId", String.valueOf(usuarioIdInexistente))
                        .param("texto", texto)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cannot invoke")));
    }

    @Test
    @DisplayName("GlobalExceptionHandler.handleRuntime maneja RuntimeException correctamente")
    void handleRuntime_manejaRuntimeException() {
        
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException ex = new RuntimeException("Error de runtime");

        var response = handler.handleRuntime(ex);
       
        assertNotNull(response, "La respuesta no debe ser nula");
        assertEquals(400, response.getStatusCodeValue(), "El código de estado debe ser 400");
        assertEquals("Error de runtime", response.getBody(), "El mensaje debe coincidir");
    }

    @Test
    @DisplayName("GlobalExceptionHandler.handleGeneral maneja Exception general correctamente")
    void handleGeneral_manejaExceptionGeneral() {
        
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Error general");
       
        var response = handler.handleGeneral(ex);
       
        assertNotNull(response, "La respuesta no debe ser nula");
        assertEquals(500, response.getStatusCodeValue(), "El código de estado debe ser 500");
        assertTrue(response.getBody().toString().contains("Error interno del servidor"), 
            "El mensaje debe contener 'Error interno del servidor'");
    }
}

