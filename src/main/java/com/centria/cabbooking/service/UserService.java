package com.centria.cabbooking.service;

import com.centria.cabbooking.entity.User;
import com.centria.cabbooking.exception.ResourceConflictException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Service interface for user management operations
 */
public interface UserService {
    /**
     * Create a new user in the system
     * @param user User entity to create
     * @return Created user with generated ID
     * @throws ResourceConflictException if email/mobile already exists
     */
    User createUser(User user);

    /**
     * Get user by ID
     * @param userId User ID
     * @return User entity
     * @throws EntityNotFoundException if user not found
     */
    User getUserById(String userId);

    /**
     * Get user by email
     * @param email User email
     * @return User entity
     * @throws EntityNotFoundException if user not found
     */
    User getUserByEmail(String email);

    /**
     * Update user status (ACTIVE/INACTIVE/SUSPENDED)
     * @param userId User ID
     * @param status New status
     * @return Updated user
     * @throws EntityNotFoundException if user not found
     */
    User updateUserStatus(String userId, User.UserStatus status);

    /**
     * Get all users by role
     * @param role User role (ADMIN/CUSTOMER/DRIVER)
     * @return List of users with specified role
     */
    List<User> getUsersByRole(User.Role role);

    /**
     * Delete user (soft delete - update status to INACTIVE)
     * @param userId User ID
     * @throws EntityNotFoundException if user not found
     */
    void deleteUser(String userId);
}