package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.UserDTO;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String userId;
    private String email;
    private String password;
    private String passwordConfirmation;

    @PersistenceCreator
    public User(String userId,String email,String password, String passwordConfirmation) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
