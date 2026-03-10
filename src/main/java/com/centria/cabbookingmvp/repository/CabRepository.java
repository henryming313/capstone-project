package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.Cab;
import com.centria.cabbookingmvp.entity.CabStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CabRepository extends JpaRepository<Cab, Long> {
    Optional<Cab> findByPlateNumber(String plateNumber);
    List<Cab> findByDriverId(Long driverId);
    List<Cab> findByStatus(CabStatus status);
}