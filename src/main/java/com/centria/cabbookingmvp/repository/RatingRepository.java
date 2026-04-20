package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByTrip_Id(Long tripId);

    List<Rating> findByDriver_Id(Long driverId);

    List<Rating> findByRider_Id(Long riderId);
}
