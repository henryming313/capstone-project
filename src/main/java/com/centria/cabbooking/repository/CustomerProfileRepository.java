package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CustomerProfile entity
 * Provides database access methods for customer profile operations
 */
@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, String> {

    /**
     * Find customer profile by associated user's userId
     * JPA naming rule: findBy[EntityProperty][SubProperty]
     * - CustomerProfile has "user" property (type: User)
     * - User has "userId" property (primary key)
     * So method name must be findByUserUserId instead of findByUserId
     *
     * @param userId the unique identifier of the associated user
     * @return CustomerProfile object matching the userId
     */
    CustomerProfile findByUserUserId(String userId);
}