package unrn.services;

import unrn.model.User;
import unrn.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    static final String ERROR_USERNAME_OBLIGATORIO =
            "El nombre de usuario es obligatorio";
    static final String ERROR_USERNAME_DUPLICADO =
            "El nombre de usuario ya existe. Por favor, elige otro nombre.";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User crearUsuario(String userName) {
        String normalized = userName != null ? userName.trim() : null;
        if (normalized == null) {
            throw new RuntimeException(ERROR_USERNAME_OBLIGATORIO);
        }
        if (userRepository.existsByUserNameIgnoreCase(normalized)) {
            throw new RuntimeException(ERROR_USERNAME_DUPLICADO);
        }
        User user = new User(normalized);
        return userRepository.save(user);
    }

    public User buscarPorId(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User buscarPorUserName(String userName) {
        return userRepository.findByUserNameIgnoreCase(userName).orElse(null);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    public void eliminarUsuario(int id) {
        userRepository.deleteById(id);
    }
}
