package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.TripBooking;
import com.centria.cabbookingmvp.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripBookingRepository extends JpaRepository<TripBooking, Long> {

    List<TripBooking> findByRider_Id(Long riderId);

    List<TripBooking> findByDriver_Id(Long driverId);

    List<TripBooking> findByStatus(TripStatus status);
}