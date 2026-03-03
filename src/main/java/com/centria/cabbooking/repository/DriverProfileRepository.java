package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for DriverProfile entity
 * Provides database access methods for driver profile operations
 */
@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, String> {

    /**
     * Find driver profile by associated user's userId
     * JPA naming rule: findBy[EntityProperty][SubProperty]
     * - DriverProfile has "user" property (type: User)
     * - User has "userId" property (primary key)
     * So method name must be findByUserUserId instead of findByUserId
     *
     * @param userId the unique identifier of the associated user
     * @return DriverProfile object matching the userId
     */
    DriverProfile findByUserUserId(String userId);

    /**
     * Find driver profile by license number (unique constraint)
     *
     * @param licenseNumber the unique driver license number
     * @return DriverProfile object matching the license number
     */
    DriverProfile findByLicenseNumber(String licenseNumber);
}