package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.model.User;

import java.util.List;

public interface UserRepository{
    void save(User user);
    List<User> findAll();
    User findById(String userId) throws UserServiceException;
    void deleteById(String userId) throws UserServiceException;
}
