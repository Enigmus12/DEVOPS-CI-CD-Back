package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user-service")

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> users(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User user (@PathVariable String id) throws UserServiceException {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public User user(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/users/{id}")
    public List<User> deleteUser(@PathVariable String id) throws UserServiceException {
        userService.deleteUser(id);
        return userService.getAllUsers();
    }
}
