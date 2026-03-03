package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.TripReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripReviewRepository extends JpaRepository<TripReview, String> {
    List<TripReview> findByTripBooking_BookingId(String bookingId);
    List<TripReview> findByDriver_User_UserId(String driverId);
    List<TripReview> findByCustomer_User_UserId(String customerId);
}