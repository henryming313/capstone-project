package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.TripBooking;
import com.centria.cabbooking.entity.CustomerProfile;
import com.centria.cabbooking.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for TripBooking entity operations
 */
@Repository
public interface TripBookingRepository extends JpaRepository<TripBooking, String> {
    // Find trips by customer and status
    List<TripBooking> findByCustomerAndStatus(CustomerProfile customer, TripBooking.BookingStatus status);

    // Find trips by driver and date range
    List<TripBooking> findByDriverAndCreatedAtBetween(DriverProfile driver,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate);

    // Find active trips for a driver
    List<TripBooking> findByDriverAndStatusIn(DriverProfile driver, List<TripBooking.BookingStatus> statuses);
}
