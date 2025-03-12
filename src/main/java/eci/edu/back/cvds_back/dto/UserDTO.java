package eci.edu.back.cvds_back.dto;

import java.time.LocalDate;

public class UserDTO {
    private String id;
    private String username;
    private int phone;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getPhone() {
        return phone;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }

}
