package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.Cab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CabRepository extends JpaRepository<Cab, Long> {
    Optional<Cab> findByPlateNumber(String plateNumber);
}