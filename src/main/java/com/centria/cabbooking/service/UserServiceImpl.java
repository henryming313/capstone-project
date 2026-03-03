package com.centria.cabbooking.service;

import com.centria.cabbooking.entity.User;
import com.centria.cabbooking.exception.ResourceConflictException;
import com.centria.cabbooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of UserService interface
 * Handles core user management business logic
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceConflictException("Email already exists: " + user.getEmail());
        }

        if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
            throw new ResourceConflictException("Mobile number already exists: " + user.getMobileNumber());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public User updateUserStatus(String userId, User.UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = getUserById(userId);
        user.setStatus(User.UserStatus.INACTIVE);
        userRepository.save(user);
    }
}