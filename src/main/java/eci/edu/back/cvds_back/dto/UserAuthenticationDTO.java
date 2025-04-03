package eci.edu.back.cvds_back.dto;

/**
 * Data Transfer Object (DTO) for user authentication.
 * This class is used to encapsulate the user's authentication details,
 * including the user ID and password.
 */
public class UserAuthenticationDTO {

    private String userId;
    private String password;

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}

