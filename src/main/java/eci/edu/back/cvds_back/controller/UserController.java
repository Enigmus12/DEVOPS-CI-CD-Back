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



/**
 * UserController is a REST controller that provides endpoints for managing users
 * and handling user-related operations such as registration, authentication, 
 * retrieval, and deletion.
 *
 * <p>It exposes the following endpoints:
 * <ul>
 *   <li>GET /user-service/users - Retrieves a list of all users.</li>
 *   <li>GET /user-service/users/{userId} - Retrieves a user by their unique identifier.</li>
 *   <li>POST /user-service/register - Registers a new user.</li>
 *   <li>DELETE /user-service/users/{userId} - Deletes a user by their unique identifier.</li>
 *   <li>POST /user-service/login - Authenticates a user based on provided credentials.</li>
 * </ul>
 *
 * <p>Each endpoint is mapped to a specific method in this controller, which interacts
 * with the {@link UserService} to perform the required operations.
 *
 * <p>Cross-origin requests are allowed for all origins.
 *
 * <p>Exceptions such as {@link UserServiceException} are thrown for error scenarios
 * during user retrieval or deletion.
 *
 * <p>Dependencies:
 * <ul>
 *   <li>{@link UserService} - Service layer for user-related operations.</li>
 * </ul>
 *
 * <p>Annotations used:
 * <ul>
 *   <li>{@link RestController} - Marks this class as a REST controller.</li>
 *   <li>{@link RequestMapping} - Maps requests to "/user-service".</li>
 *   <li>{@link CrossOrigin} - Enables cross-origin requests.</li>
 *   <li>{@link Autowired} - Injects the UserService dependency.</li>
 * </ul>
 */
@RestController
@RequestMapping("/user-service")
@CrossOrigin(origins = "*")
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
     * @param userId The unique identifier of the user to retrieve.
     * @return The User object corresponding to the provided userId.
     * @throws UserServiceException If an error occurs while retrieving the user.
     */
    @GetMapping("/users/{userId}")
    public User user (@PathVariable String userId) throws UserServiceException {
        return userService.getUser(userId);
    }


    /**
     * Handles the HTTP POST request to register a new user.
     *
     * @param user The UserDTO object containing the details of the user to be registered.
     * @return The saved User object after successful registration.
     */
    @PostMapping("/register")
    public User user(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }


    /**
     * Deletes a user by their unique identifier and returns the updated list of all users.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @return A list of all remaining users after the specified user has been deleted.
     * @throws UserServiceException If an error occurs while attempting to delete the user.
     */
    @DeleteMapping("/users/{userId}")
    public List<User> deleteUser(@PathVariable String userId) throws UserServiceException {
        userService.deleteUser(userId);
        return userService.getAllUsers();
    }

    /**
     * Handles the login request by authenticating the user based on the provided
     * credentials.
     *
     * @param authenticationDTO The data transfer object containing the user's
     *                          authentication credentials (e.g., username and password).
     * @return An {@link AuthenticationResponseDTO} containing the authentication
     *         result, such as a token or user details.
     */
    @PostMapping("/login")
    public AuthenticationResponseDTO authenticate(@RequestBody UserAuthenticationDTO authenticationDTO) {
        return userService.authenticate(authenticationDTO);
    }

}
