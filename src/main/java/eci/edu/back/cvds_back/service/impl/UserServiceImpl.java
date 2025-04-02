package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.controller.JwtUtil;
import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.UserAuthenticationDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import eci.edu.back.cvds_back.service.interfaces.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User getUser(String userId) throws UserServiceException {
        return userRepository.findById(userId);
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User(userDTO);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String userId) throws UserServiceException {
        userRepository.deleteById(userId);
    }

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