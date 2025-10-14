package unrn.services;

import unrn.model.User;
import unrn.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void crearUsuario(String userName) {
        User user = new User(userName);
        userRepository.guardar(user);
    }

    public User buscarPorId(int id) {
        return userRepository.buscarPorId(id);
    }

    public User buscarPorUserName(String userName) {
        return userRepository.buscarPorUserName(userName);
    }
}
