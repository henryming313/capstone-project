package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.User;
// Import the User entity class
import org.springframework.data.jpa.repository.JpaRepository;
// Interfaces provided by Spring Data JPA
// Automatically provide database CRUD operations

import java.util.Optional;

// UserRepository is the user database operation interface
// Inheriting from JpaRepository automatically provides CRUD functionality
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
}