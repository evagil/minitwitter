package unrn.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unrn.dto.UserDto;
import unrn.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    @DisplayName("UserMapper.toDto convierte User a UserDto correctamente")
    void toDto_convierteUserAUserDto() {
        
        User user = new User("usuarioTest");
        user.setId(1);
        
        UserDto dto = UserMapper.toDto(user);
       
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertEquals(user.getId(), dto.getId(), "El ID debe coincidir");
        assertEquals(user.getUserName(), dto.getUserName(), "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("UserMapper.toDto con User null lanza NullPointerException")
    void toDto_userNull_lanzaExcepcion() {
        
        User user = null;
       
        assertThrows(NullPointerException.class, () -> {
            UserMapper.toDto(user);
        }, "Debe lanzar NullPointerException con User null");
    }
}

