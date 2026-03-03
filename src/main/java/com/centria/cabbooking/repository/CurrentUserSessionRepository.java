package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.CurrentUserSession;
import com.centria.cabbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CurrentUserSession entity operations
 */
@Repository
public interface CurrentUserSessionRepository extends JpaRepository<CurrentUserSession, String> {
    // Find session by user and active status (expiresAt > now)
    Optional<CurrentUserSession> findByUserAndExpiresAtAfter(User user, java.time.LocalDateTime now);

    // Find session by refresh token
    Optional<CurrentUserSession> findByRefreshToken(String refreshToken);

    // Delete all expired sessions
    void deleteByExpiresAtBefore(java.time.LocalDateTime now);
}