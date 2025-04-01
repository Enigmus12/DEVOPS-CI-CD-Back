package eci.edu.back.cvds_back.dto;

import eci.edu.back.cvds_back.model.User;

public class AuthenticationResponseDTO {
    private String token;
    private boolean authenticated;
    private User userId;
    private String message;

    public AuthenticationResponseDTO(String token , boolean authenticated, User userId, String message) {
        this.token = token;
        this.authenticated = authenticated;
        this.userId = userId;
        this.message = message;
    }

    public AuthenticationResponseDTO() {
        //TODO Auto-generated constructor stub
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getEmail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmail'");
    }

    public Object getPassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }
}
