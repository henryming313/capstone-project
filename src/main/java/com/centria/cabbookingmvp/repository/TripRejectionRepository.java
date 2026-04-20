package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.TripRejection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRejectionRepository extends JpaRepository<TripRejection, Long> {

    boolean existsByTrip_IdAndDriver_Id(Long tripId, Long driverId);
}
