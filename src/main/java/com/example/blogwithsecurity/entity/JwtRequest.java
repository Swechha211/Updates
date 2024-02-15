package com.example.blogwithsecurity.entity;

public class JwtRequest {
    private String email;
    private String Password;

    public JwtRequest() {
    }

    public JwtRequest(String email, String password) {
        this.email = email;
        Password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
