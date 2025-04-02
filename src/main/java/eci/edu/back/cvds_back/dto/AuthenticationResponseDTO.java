package eci.edu.back.cvds_back.dto;

import eci.edu.back.cvds_back.model.User;

public class AuthenticationResponseDTO {
    private boolean authenticated;
    private User user;
    private String token;
    private String message;

    public AuthenticationResponseDTO(boolean authenticated, User user, String token, String message) {
        this.authenticated = authenticated;
        this.user = user;
        this.token = token;
        this.message = message;
    }

    public boolean isAuthenticated() {return authenticated;}
    public void setAuthenticated(boolean authenticated) {this.authenticated = authenticated;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
}