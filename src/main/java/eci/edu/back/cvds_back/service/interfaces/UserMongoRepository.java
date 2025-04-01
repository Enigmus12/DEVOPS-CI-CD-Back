package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.model.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMongoRepository extends MongoRepository<User, String>{

    Optional <User> findByEmail(String email);

}
