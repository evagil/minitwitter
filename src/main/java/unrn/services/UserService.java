package unrn.services;

import unrn.model.User;
import unrn.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User crearUsuario(String userName) {
        String normalized = userName != null ? userName.trim() : null;
        if (normalized == null) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (userRepository.existsByUserNameIgnoreCase(normalized)) {
            throw new RuntimeException("El nombre de usuario ya existe");
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
