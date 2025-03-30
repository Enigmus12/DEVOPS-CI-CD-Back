package eci.edu.back.cvds_back.dto;

import java.time.LocalDate;

/**
 * The UserDTO class represents a Data Transfer Object for user information.
 * It contains basic user details such as ID, username, and phone number.
 */
public class UserDTO {
    private String id;
    private String username;
    private int phone;

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the user's unique identifier as a String.
     */
    public String getId() {
        return id;
    }
    /**
     * Sets the ID of the user.
     *
     * @param id the unique identifier to set for the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username as a String.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username for the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return the phone number as an integer.
     */
    public int getPhone() {
        return phone;
    }
    /**
     * Sets the phone number for the user.
     *
     * @param phone the phone number to set
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }

}
