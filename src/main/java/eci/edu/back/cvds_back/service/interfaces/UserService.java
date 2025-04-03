package eci.edu.back.cvds_back.service.interfaces;


import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.UserAuthenticationDTO;
import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return The user associated with the given identifier.
     * @throws UserServiceException If the user cannot be found or an error occurs during retrieval.
     */
    User getUser(String userId) throws UserServiceException;

    /**
     * Saves a new user or updates an existing user.
     *
     * @param user The data transfer object containing user information.
     * @return The saved or updated user.
     */
    User saveUser(UserDTO user);

    /**
     * Retrieves a list of all users.
     *
     * @return A list of all users.
     */
    List<User> getAllUsers();

    /**
     * Deletes a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @throws UserServiceException If the user cannot be deleted or an error occurs during deletion.
     */
    void deleteUser(String userId) throws UserServiceException;

    /**
     * Authenticates a user based on their credentials.
     *
     * @param authenticationDTO The data transfer object containing authentication credentials.
     * @return The authentication response containing relevant authentication details.
     */
    AuthenticationResponseDTO authenticate(UserAuthenticationDTO authenticationDTO);
}
