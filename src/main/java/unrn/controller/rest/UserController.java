package unrn.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.model.User;
import unrn.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestParam String userName) {
        try {
            User u = userService.crearUsuario(userName);
            return ResponseEntity.ok(u);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<User> listar() {
        return userService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        User u = userService.buscarPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        User u = userService.buscarPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        userService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado");
    }
}


