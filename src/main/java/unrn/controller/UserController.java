package unrn.controller;

import unrn.services.UserService;
import unrn.model.User;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void crearUsuario(String userName) {
        userService.crearUsuario(userName);
    }

    public User buscarPorId(int id) {
        return userService.buscarPorId(id);
    }

    public User buscarPorUserName(String userName) {
        return userService.buscarPorUserName(userName);
    }
}
