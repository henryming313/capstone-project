package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.DriverCabAssignment;
import com.centria.cabbooking.entity.DriverProfile;
import com.centria.cabbooking.entity.Cab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for DriverCabAssignment entity operations
 */
@Repository
public interface DriverCabAssignmentRepository extends JpaRepository<DriverCabAssignment, String> {
    // Find active assignment for a driver
    Optional<DriverCabAssignment> findByDriverAndIsActive(DriverProfile driver, Boolean isActive);

    // Find active assignment for a cab
    Optional<DriverCabAssignment> findByCabAndIsActive(Cab cab, Boolean isActive);
}
