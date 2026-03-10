package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.entity.User;
import com.centria.cabbookingmvp.entity.UserStatus;
import com.centria.cabbookingmvp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User register(User user) {
        // 1) Basic verification
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // 2) Plagiarism check
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepo.findByPhone(user.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone already exists");
        }

        //
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        //
        return userRepo.save(user);
    }

    @Override
    public User login(String identifier, String password) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }


        User user = userRepo.findByEmail(identifier)
                .orElseGet(() -> userRepo.findByPhone(identifier).orElse(null));

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }


        if (!password.equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }

    @Override
    public User getById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}