package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.UserAuthenticationDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/user-service")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public List<User> users(){
        return userService.getAllUsers();
    }


    @GetMapping("/users/{userId}")
    public User user (@PathVariable String userId) throws UserServiceException {
        return userService.getUser(userId);
    }


    @PostMapping("/register")
    public AuthenticationResponseDTO user(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }


    @DeleteMapping("/users/{userId}")
    public List<User> deleteUser(@PathVariable String userId) throws UserServiceException {
        userService.deleteUser(userId);
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO authenticate(@RequestBody UserAuthenticationDTO authenticationDTO) {
        return userService.authenticate(authenticationDTO);
    }

}
