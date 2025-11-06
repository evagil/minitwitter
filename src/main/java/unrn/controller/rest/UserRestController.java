package unrn.controller.rest;

import unrn.dto.UserDTO;
import unrn.model.User;
import unrn.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<User> users = userService.listarTodos();
            List<UserDTO> dtos = users.stream()
                    .map(u -> new UserDTO(u.getId(), u.getUserName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al listar usuarios");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("cause", e.getCause() != null ? e.getCause().getMessage() : "Desconocido");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable int id) {
        User user = userService.buscarPorId(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUserName()));
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody UserDTO userDTO) {
        try {
            userService.crearUsuario(userDTO.getUserName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

