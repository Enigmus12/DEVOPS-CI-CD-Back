package eci.edu.back.cvds_back.service.interfaces;


import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.dto.UserAuthenticationDTO;
import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;

import java.util.List;

public interface UserService {
    User getUser(String userId) throws UserServiceException;
    AuthenticationResponseDTO saveUser(UserDTO user);
    List<User> getAllUsers();
    void deleteUser(String userId) throws UserServiceException;
    AuthenticationResponseDTO authenticate(UserAuthenticationDTO authenticationDTO);
}
