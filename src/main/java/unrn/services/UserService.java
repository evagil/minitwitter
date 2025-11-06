package unrn.services;

import unrn.model.User;
import unrn.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void crearUsuario(String userName) {
        // Verificar si ya existe
        Optional<User> existente = userRepository.findByUserName(userName);
        if (existente.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        User user = new User(userName);
        userRepository.save(user);
    }

    public User buscarPorId(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User buscarPorUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    public void eliminarUsuario(int id) {
        userRepository.deleteById(id);
    }
}
