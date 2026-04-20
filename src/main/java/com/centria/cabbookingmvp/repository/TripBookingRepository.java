package com.centria.cabbookingmvp.repository;

import com.centria.cabbookingmvp.entity.TripBooking;
import com.centria.cabbookingmvp.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

import java.util.List;
// TripBookingRepository is the order database operation interface
// Inheriting from JpaRepository automatically provides CRUD functionality
public interface TripBookingRepository extends JpaRepository<TripBooking, Long> {

    List<TripBooking> findByRider_Id(Long riderId);

    List<TripBooking> findByDriver_Id(Long driverId);

    List<TripBooking> findByStatus(TripStatus status);
    long countByDriver_IdAndStatus(Long driverId, TripStatus status);

    @Query("select coalesce(sum(t.totalFare), 0) from TripBooking t where t.driver.id = :driverId and t.status = com.centria.cabbookingmvp.entity.TripStatus.COMPLETED")
    BigDecimal sumCompletedFareByDriver(@Param("driverId") Long driverId);
    @Query("""
    select t from TripBooking t
    where t.status = com.centria.cabbookingmvp.entity.TripStatus.PENDING
      and not exists (
        select 1 from TripRejection r
        where r.trip.id = t.id and r.driver.id = :driverId
      )
""")
    java.util.List<TripBooking> findPendingTripsExcludingRejected(@Param("driverId") Long driverId);
}

