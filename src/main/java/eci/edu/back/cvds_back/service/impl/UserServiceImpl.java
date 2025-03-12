package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.UserServiceException;
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

    @Override
    public User getUser(String id) throws UserServiceException {
        return userRepository.findById(id);
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
    public void deleteUser(String id) throws UserServiceException {
        userRepository.deleteById(id);
    }

}
