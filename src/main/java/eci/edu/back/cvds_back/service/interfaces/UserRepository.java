package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.model.User;

import java.util.List;

public interface UserRepository{
    void save(User user);
    List<User> findAll();
    User findById(String id) throws UserServiceException;
    void deleteById(String id) throws UserServiceException;
}
