package com.centria.cabbooking.service;

import com.centria.cabbooking.entity.TripBooking;
import com.centria.cabbooking.entity.CustomerProfile;
import com.centria.cabbooking.entity.DriverProfile;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for trip booking operations
 */
public interface TripBookingService {
    /**
     * Create a new trip booking request
     * @param tripBooking Trip booking details
     * @return Created trip booking
     */
    TripBooking createBooking(TripBooking tripBooking);

    /**
     * Assign driver and cab to a pending booking
     * @param bookingId Booking ID
     * @param driverId Driver ID
     * @param cabId Cab ID
     * @return Updated booking with assigned driver/cab
     * @throws EntityNotFoundException if booking/driver/cab not found
     */
    TripBooking assignDriverAndCab(String bookingId, String driverId, String cabId);

    /**
     * Start an assigned trip
     * @param bookingId Booking ID
     * @return Updated booking with status ON_GOING
     * @throws EntityNotFoundException if booking not found
     */
    TripBooking startTrip(String bookingId);

    /**
     * Complete a trip and calculate final fare
     * @param bookingId Booking ID
     * @param distanceKm Actual distance traveled
     * @return Updated booking with status COMPLETED and final fare
     * @throws EntityNotFoundException if booking not found
     */
    TripBooking completeTrip(String bookingId, Double distanceKm);

    /**
     * Cancel a pending/confirmed booking
     * @param bookingId Booking ID
     * @return Updated booking with status CANCELLED
     * @throws EntityNotFoundException if booking not found
     */
    TripBooking cancelBooking(String bookingId);

    /**
     * Calculate trip fare based on distance, base fare and surge multiplier
     * @param baseFare Base fare
     * @param distanceKm Travel distance
     * @param surgeMultiplier Surge pricing multiplier
     * @return Calculated total fare
     */
    BigDecimal calculateFare(BigDecimal baseFare, Double distanceKm, BigDecimal surgeMultiplier);

    /**
     * Get all bookings for a customer
     * @param customerId Customer ID
     * @return List of customer's bookings
     */
    List<TripBooking> getCustomerBookings(String customerId);

    /**
     * Get all bookings for a driver in a date range
     * @param driverId Driver ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of driver's bookings in the date range
     */
    List<TripBooking> getDriverBookings(String driverId, LocalDateTime startDate, LocalDateTime endDate);
}