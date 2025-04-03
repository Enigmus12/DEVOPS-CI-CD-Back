package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.interfaces.UserMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserRepository interface that provides methods
 * to interact with the User data stored in a MongoDB database.
 * This class uses the UserMongoRepository to perform CRUD operations.
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #save(User)}: Saves a User object to the database.</li>
 *   <li>{@link #findAll()}: Retrieves all User objects from the database.</li>
 *   <li>{@link #findById(String)}: Finds a User by its ID, throws an exception if not found.</li>
 *   <li>{@link #deleteById(String)}: Deletes a User by its ID.</li>
 * </ul>
 * 
 * <p>Exceptions:</p>
 * <ul>
 *   <li>{@link UserServiceException}: Thrown when a User is not found during retrieval or deletion.</li>
 * </ul>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>{@code @Service}: Marks this class as a Spring service component.</li>
 *   <li>{@code @Autowired}: Injects the UserMongoRepository dependency.</li>
 * </ul>
 */
@Service
public class UserRepositoryImpl implements UserRepository{
    @Autowired
    private UserMongoRepository userMongoRepository;

    /**
     * Saves the given user entity to the database.
     *
     * @param user the user entity to be saved
     */
    @Override
    public void save(User user) {
        userMongoRepository.save(user);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users stored in the database.
     */
    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The User object corresponding to the given userId.
     * @throws UserServiceException If no user is found with the given userId.
     */
    @Override
    public User findById(String userId) throws UserServiceException {
        Optional<User> user = userMongoRepository.findById(userId);
        if(user.isEmpty()) throw new UserServiceException("User Not found");
        return user.get();
    }

    /**
     * Deletes a user from the repository based on the provided user ID.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @throws UserServiceException If an error occurs during the deletion process.
     */
    @Override
    public void deleteById(String userId) throws UserServiceException {
        userMongoRepository.deleteById(userId);
    }

}
