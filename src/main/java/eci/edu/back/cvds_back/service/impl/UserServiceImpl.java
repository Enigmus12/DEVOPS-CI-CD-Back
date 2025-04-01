package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.UserAuthenticationDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import eci.edu.back.cvds_back.jwt.JwtService;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Override
    public User getUser(String userId) throws UserServiceException {
        return userRepository.findById(userId);
    }

    @Override
    public AuthenticationResponseDTO saveUser(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        return new AuthenticationResponseDTO(jwtService.getToken(user.getEmail()),true, user, "Autenticación exitosa");
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
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword())
            );
            // Buscar usuario por nombre de usuario
            User user = userRepository.findByEmail(authenticationDTO.getEmail());
            return new AuthenticationResponseDTO(jwtService.getToken(user.getEmail()),true, user, "Autenticación exitosa");
          
        } catch (UserServiceException e) {
            // Usuario no encontrado
            return new AuthenticationResponseDTO(null,false, null, "Email/contraseña incorrecta");
        }
    }

}
