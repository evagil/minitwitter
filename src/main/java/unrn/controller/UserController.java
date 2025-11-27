package unrn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.dto.UserDto;
import unrn.mapper.UserMapper;
import unrn.model.User;
import unrn.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin
public class UserController {

    static final String MENSAJE_USUARIO_ELIMINADO = "Usuario eliminado";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestParam String userName) {
        try {
            User u = userService.crearUsuario(userName);
            return ResponseEntity.ok(UserMapper.toDto(u));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<UserDto> listar() {
        return userService.listarTodos().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        User u = userService.buscarPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.toDto(u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        User u = userService.buscarPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        userService.eliminarUsuario(id);
        return ResponseEntity.ok(MENSAJE_USUARIO_ELIMINADO);
    }
}


