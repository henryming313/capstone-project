package com.centria.cabbookingmvp.controller.dto;

import jakarta.validation.constraints.NotBlank;

//Login request object
//Used to receive login data sent by the client
public class LoginRequest {

    // Log in to account
    @NotBlank
    private String identifier;

    // Password entered by the user
    @NotBlank
    private String password;

    // Set up login account
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    // Get Password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}