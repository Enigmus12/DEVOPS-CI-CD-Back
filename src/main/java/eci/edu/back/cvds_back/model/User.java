package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.UserDTO;
import jakarta.persistence.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "users")
public class User {
    private String id;
    private String username;
    private int phone;

    @PersistenceCreator
    public User(String id,String username, int phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.phone = userDTO.getPhone();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getPhone() { return phone; }
    public void setPhone(int phone) { this.phone = phone; }

}
