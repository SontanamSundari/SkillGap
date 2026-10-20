package com.skillgap.auth.dto;

public class RegisterResponse {

    private String id;
    private String name;
    private String email;
    private String role;

    public RegisterResponse() {
    }

    public RegisterResponse(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}