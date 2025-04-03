package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.UserAuthenticationDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import eci.edu.back.cvds_back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The User object corresponding to the provided userId.
     * @throws UserServiceException If an error occurs while retrieving the user.
     */
    @Override
    public User getUser(String userId) throws UserServiceException {
        return userRepository.findById(userId);
    }

    /**
     * Saves a new user to the repository.
     *
     * @param userDTO The data transfer object containing user information to be saved.
     * @return The saved User entity.
     */
    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User(userDTO);
        userRepository.save(user);
        return user;
    }

    /**
     * Retrieves a list of all users from the repository.
     *
     * @return a list containing all users.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user from the system based on the provided user ID.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @throws UserServiceException If an error occurs during the deletion process.
     */
    @Override
    public void deleteUser(String userId) throws UserServiceException {
        userRepository.deleteById(userId);
    }

    /**
     * Authenticates a user based on the provided authentication details.
     *
     * @param authenticationDTO The data transfer object containing the user's authentication details,
     *                          including user ID and password.
     * @return An {@link AuthenticationResponseDTO} containing the authentication result:
     *         - If authentication is successful, it includes the user details, a generated JWT token,
     *           and a success message.
     *         - If authentication fails due to incorrect password or user not found, it includes
     *           an appropriate failure message.
     * @throws UserServiceException If an error occurs while retrieving the user from the repository.
     */
    @Override
    public AuthenticationResponseDTO authenticate(UserAuthenticationDTO authenticationDTO) {
        try {
            // Buscar usuario por nombre de usuario
            User user = userRepository.findById(authenticationDTO.getUserId());

            // Verificar la contraseña
            if (user.getPassword().equals(authenticationDTO.getPassword())) {
                // Generar token JWT
                String token = jwtUtil.generateToken(user.getUserId());
                return new AuthenticationResponseDTO(true, user, token, "Autenticación exitosa");
            } else {
                return new AuthenticationResponseDTO(false, null, null, "Contraseña incorrecta");
            }
        } catch (UserServiceException e) {
            // Usuario no encontrado
            return new AuthenticationResponseDTO(false, null, null, "Usuario no encontrado");
        }
    }
}