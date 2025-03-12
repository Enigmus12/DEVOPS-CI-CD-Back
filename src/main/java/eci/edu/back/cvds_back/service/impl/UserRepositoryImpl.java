package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository{
    @Autowired
    private UserMongoRepository userMongoRepository;

    @Override
    public void save(User user) {
        userMongoRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll();
    }

    @Override
    public User findById(String id) throws UserServiceException {
        Optional<User> user = userMongoRepository.findById(id);
        if(user.isEmpty()) throw new UserServiceException("User Not found");
        return user.get();
    }

    @Override
    public void deleteById(String id) throws UserServiceException {
        userMongoRepository.deleteById(id);
    }

}
