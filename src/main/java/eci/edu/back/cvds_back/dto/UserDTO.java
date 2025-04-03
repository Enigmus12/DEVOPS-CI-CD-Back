package eci.edu.back.cvds_back.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for representing a user.
 * This class is used to encapsulate user-related data and provide
 * getter and setter methods for accessing and modifying the data.
 */
public class UserDTO {
    private String userId;
    private String email;
    private String password;
    private String passwordConfirmation;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {this.password = password;}
    public String getPasswordConfirmation() {return passwordConfirmation;}
    public void setPasswordConfirmation(String passwordConfirmation) {this.passwordConfirmation = passwordConfirmation;}

}
