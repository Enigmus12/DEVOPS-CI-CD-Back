package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.UserDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Represents a user entity in the system.
 * This class is annotated with @Document to indicate that it is a MongoDB document
 * stored in the "users" collection.
 * 
 * Each user has the following attributes:
 * - userId: A unique identifier for the user.
 * - email: The email address associated with the user.
 * - password: The password for the user's account.
 * - passwordConfirmation: A confirmation of the user's password.
 * 
 * The class provides constructors for creating a user instance either directly
 * with its attributes or using a UserDTO object. It also includes getter and setter
 * methods for accessing and modifying the attributes.
 */
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String email;
    private String password;
    private String passwordConfirmation;

    /**
     * Constructs a new User object with the specified details.
     *
     * @param userId               The unique identifier for the user.
     * @param email                The email address of the user.
     * @param password             The password for the user account.
     * @param passwordConfirmation The confirmation of the user's password.
     */
    @PersistenceCreator
    public User(String userId,String email,String password, String passwordConfirmation) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    /**
     * Constructs a new User object using the provided UserDTO.
     *
     * @param userDTO the UserDTO object containing the user details
     *                such as userId, email, password, and password confirmation.
     */
    public User(UserDTO userDTO) {
        this.userId = userDTO.getUserId();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.passwordConfirmation = userDTO.getPasswordConfirmation();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirmation() { return passwordConfirmation; }
    public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }

}
