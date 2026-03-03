package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNumber(String mobileNumber);
    Optional<User> findByUserIdAndRole(String userId, User.Role role);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);

    List<User> findByRole(User.Role role);
}