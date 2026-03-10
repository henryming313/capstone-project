package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.entity.User;

public interface UserService {

    // Registration: Plagiarism Check + Save
    User register(User user);

    // Login: The identifier can be an email address or a phone number.
    User login(String identifier, String password);

    // Search for users by ID
    User getById(Long id);
}
