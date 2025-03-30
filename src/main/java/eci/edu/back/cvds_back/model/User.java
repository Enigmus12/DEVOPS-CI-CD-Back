package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.UserDTO;
import jakarta.persistence.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Represents a User entity in the system.
 * This class is mapped to the "users" collection in the database.
 * It contains user-related information such as ID, username, and phone number.
 */
@Document(collection = "users")
public class User {
    private String id;
    private String username;
    private int phone;

    /**
     * Constructs a new User instance with the specified id, username, and phone number.
     *
     * @param id       the unique identifier of the user
     * @param username the username of the user
     * @param phone    the phone number of the user
     */
    @PersistenceCreator
    public User(String id,String username, int phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

    /**
     * Constructs a new User object using the data from a UserDTO object.
     *
     * @param userDTO the UserDTO object containing the data to initialize the User object
     */
    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.phone = userDTO.getPhone();
    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the user's unique identifier as a String.
     */
    public String getId() { return id; }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the unique identifier to set for the user
     */
    public void setId(String id) { this.id = id; }

    /**
     * Retrieves the username of the user.
     *
     * @return the username as a String.
     */
    public String getUsername() { return username; }

    /**
     * Sets the username for the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Retrieves the phone number of the user.
     *
     * @return the phone number as an integer.
     */
    public int getPhone() { return phone; }

    /**
     * Sets the phone number for the user.
     *
     * @param phone the phone number to set
     */
    public void setPhone(int phone) { this.phone = phone; }

}
