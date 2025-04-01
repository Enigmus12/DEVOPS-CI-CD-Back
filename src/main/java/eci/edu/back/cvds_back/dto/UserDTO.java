package eci.edu.back.cvds_back.dto;

import java.time.LocalDate;

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
