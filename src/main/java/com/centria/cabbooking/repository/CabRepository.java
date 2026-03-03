package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.Cab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Cab entity operations
 */
@Repository
public interface CabRepository extends JpaRepository<Cab, String> {
    // Find cab by registration number
    Cab findByCarNumber(String carNumber);

    // Find available cabs by status and type
    List<Cab> findByStatusAndCarType(Cab.CabStatus status, Cab.CarType carType);
}
