package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * UserController is a REST controller that provides endpoints for managing users.
 * It handles HTTP requests for retrieving, creating, and deleting user data.
 * 
 * Endpoints:
 * - GET /user-service/users: Retrieves a list of all users.
 * - GET /user-service/users/{id}: Retrieves a specific user by their ID.
 * - POST /user-service/users: Creates a new user with the provided data.
 * - DELETE /user-service/users/{id}: Deletes a user by their ID and returns the updated list of users.
 * 
 * Dependencies:
 * - UserService: Service layer used to perform operations on user data.
 * 
 * Exceptions:
 * - UserServiceException: Thrown when an error occurs in the user service layer.
 */
@RestController
@RequestMapping("/user-service")

public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of User objects containing information about all users.
     */
    @GetMapping("/users")
    public List<User> users(){
        return userService.getAllUsers();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return The user object corresponding to the provided ID.
     * @throws UserServiceException If an error occurs while retrieving the user.
     */
    @GetMapping("/users/{id}")
    public User user (@PathVariable String id) throws UserServiceException {
        return userService.getUser(id);
    }

    /**
     * Handles the HTTP POST request to create a new user.
     *
     * @param user The UserDTO object containing the details of the user to be created.
     * @return The created User object after being saved by the user service.
     */
    @PostMapping("/users")
    public User user(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }

    /**
     * Deletes a user by their unique identifier and returns the updated list of all users.
     *
     * @param id The unique identifier of the user to be deleted.
     * @return A list of all remaining users after the specified user has been deleted.
     * @throws UserServiceException If an error occurs during the deletion process.
     */
    @DeleteMapping("/users/{id}")
    public List<User> deleteUser(@PathVariable String id) throws UserServiceException {
        userService.deleteUser(id);
        return userService.getAllUsers();
    }
}
