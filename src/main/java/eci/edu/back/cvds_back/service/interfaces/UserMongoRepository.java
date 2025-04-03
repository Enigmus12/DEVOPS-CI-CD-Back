package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities in a MongoDB database.
 * Extends the MongoRepository interface to provide CRUD operations and
 * additional query methods for the User entity.
 *
 * @author esteban.aguilera
 * @see org.springframework.data.mongodb.repository.MongoRepository
 * @see eci.edu.back.cvds_back.model.User
 */
@Repository
public interface UserMongoRepository extends MongoRepository<User, String>{

}
