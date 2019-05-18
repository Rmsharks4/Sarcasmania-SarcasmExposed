package com.fyp.sarcasmania.app.Classes;

import java.io.Serializable;

public class User implements Serializable {

    private String username; //unique
    private String fullname;
    private String email;
    private String password;
    private String picture;
    private String type;

    public User() {
    }

    public User(String username, String fullname, String email, String password, String picture, String type) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
