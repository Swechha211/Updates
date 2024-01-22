package com.example.blogjdbcnew.entities;

import org.springframework.stereotype.Component;


public class User {

    private int id;
    private String name;
    private String password;
    private String email;

    private String about;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User() {
    }

    public User(int id, String name, String password, String email, String about) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.about = about;
    }
}
