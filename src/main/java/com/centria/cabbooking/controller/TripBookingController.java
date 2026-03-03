package com.centria.cabbooking.controller;

import com.centria.cabbooking.entity.*;
import com.centria.cabbooking.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for trip booking management operations
 * Handles full trip lifecycle: create booking, assign driver, start trip, complete trip, pay, review
 * Core APIs: create, assign, start, complete, pay, review, query, update
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class TripBookingController {

    // Original repositories
    private final TripBookingRepository tripBookingRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CabRepository cabRepository;

    // Added repositories for payment and review
    private final TripPaymentRepository tripPaymentRepository;
    private final TripReviewRepository tripReviewRepository;

    /**
     * Create a new trip booking (customer initiates ride request)
     * POST /api/v1/bookings
     * @param tripBooking TripBooking object with customer ID, pickup/dropoff location, base fare
     * @return Created TripBooking object with HTTP 201 status
     * @throws EntityNotFoundException if customer not found
     */
    @PostMapping
    public ResponseEntity<TripBooking> createBooking(@Valid @RequestBody TripBooking tripBooking) {
        // Validate customer exists
        CustomerProfile customer = customerProfileRepository.findById(tripBooking.getCustomer().getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + tripBooking.getCustomer().getCustomerId()));

        tripBooking.setCustomer(customer);
        tripBooking.setStatus(TripBooking.BookingStatus.PENDING);
        // Remove setBookingTime - entity has no this method (use original createdAt)
        tripBooking.setCreatedAt(LocalDateTime.now());

        TripBooking createdBooking = tripBookingRepository.save(tripBooking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /**
     * Assign driver and cab to booking (dispatch system)
     * PATCH /api/v1/bookings/{bookingId}/assign
     * @param bookingId Unique ID of the booking
     * @param driverId ID of the driver to assign
     * @param cabId ID of the cab to assign
     * @return Updated TripBooking object with HTTP 200 status
     * @throws EntityNotFoundException if booking/driver/cab not found
     * @throws IllegalStateException if driver is not online or cab is busy
     */
    @PatchMapping("/{bookingId}/assign")
    public ResponseEntity<TripBooking> assignDriverToBooking(
            @PathVariable String bookingId,
            @RequestParam String driverId,
            @RequestParam String cabId) {

        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        // Validate booking status (only PENDING can be assigned)
        if (!booking.getStatus().equals(TripBooking.BookingStatus.PENDING)) {
            throw new IllegalStateException("Only PENDING bookings can be assigned to drivers");
        }

        // Validate driver is online
        DriverProfile driver = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverId));
        if (!driver.getCurrentStatus().equals(DriverProfile.DriverStatus.ONLINE)) {
            throw new IllegalStateException("Driver is not available (not online)");
        }

        // Validate cab is available
        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found with ID: " + cabId));
        if (!cab.getStatus().equals(Cab.CabStatus.AVAILABLE)) {
            throw new IllegalStateException("Cab is not available (not in AVAILABLE status)");
        }

        // Update booking info
        booking.setDriver(driver);
        booking.setCab(cab);
        booking.setStatus(TripBooking.BookingStatus.CONFIRMED);
        // Remove setAssignTime - entity has no this method

        // Update driver and cab status (fix BUSY → original ON_TRIP)
        driver.setCurrentStatus(DriverProfile.DriverStatus.ON_TRIP);
        driverProfileRepository.save(driver);

        cab.setStatus(Cab.CabStatus.ON_TRIP);
        cabRepository.save(cab);

        TripBooking updatedBooking = tripBookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Start trip (driver confirms trip initiation)
     * PATCH /api/v1/bookings/{bookingId}/start
     * @param bookingId Unique ID of the booking
     * @return Updated TripBooking object with HTTP 200 status
     * @throws EntityNotFoundException if booking not found
     * @throws IllegalStateException if booking is not confirmed
     */
    @PatchMapping("/{bookingId}/start")
    public ResponseEntity<TripBooking> startTrip(@PathVariable String bookingId) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        // Fix IN_PROGRESS → original ON_GOING
        if (!booking.getStatus().equals(TripBooking.BookingStatus.CONFIRMED)) {
            throw new IllegalStateException("Only CONFIRMED bookings can be started");
        }

        booking.setStatus(TripBooking.BookingStatus.ON_GOING);
        booking.setStartedAt(LocalDateTime.now()); // Use original startedAt field

        TripBooking updatedBooking = tripBookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Complete trip (driver confirms trip completion)
     * PATCH /api/v1/bookings/{bookingId}/complete
     * @param bookingId Unique ID of the booking
     * @param distanceKm Actual travel distance in kilometers
     * @return Updated TripBooking object with HTTP 200 status
     * @throws EntityNotFoundException if booking not found
     * @throws IllegalStateException if booking is not in progress
     */
    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<TripBooking> completeTrip(
            @PathVariable String bookingId,
            @RequestParam Double distanceKm) {

        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        // Fix IN_PROGRESS → original ON_GOING
        if (!booking.getStatus().equals(TripBooking.BookingStatus.ON_GOING)) {
            throw new IllegalStateException("Only ON_GOING bookings can be completed");
        }

        // Calculate total fare (fix BigDecimal operation error, use entity's calculate logic)
        BigDecimal perKmRate = booking.getCab().getPerKmRate() != null ? booking.getCab().getPerKmRate() : BigDecimal.TEN;
        BigDecimal distanceFare = perKmRate.multiply(BigDecimal.valueOf(distanceKm)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalFare = booking.getBaseFare().add(distanceFare).multiply(booking.getSurgeMultiplier()).setScale(2, RoundingMode.HALF_UP);

        // Update booking info
        booking.setStatus(TripBooking.BookingStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now()); // Use original completedAt field
        booking.setDistanceKm(distanceKm);
        booking.setTotalFare(totalFare); // Fix double → BigDecimal type error

        // Restore driver and cab status
        DriverProfile driver = booking.getDriver();
        driver.setCurrentStatus(DriverProfile.DriverStatus.ONLINE);
        driverProfileRepository.save(driver);

        Cab cab = booking.getCab();
        cab.setStatus(Cab.CabStatus.AVAILABLE);
        cabRepository.save(cab);

        TripBooking updatedBooking = tripBookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Create trip payment record (customer pays for trip)
     * POST /api/v1/bookings/{bookingId}/pay
     * @param bookingId Unique ID of the booking
     * @param tripPayment TripPayment object with payment method and amount
     * @return Created TripPayment object with HTTP 201 status
     * @throws EntityNotFoundException if booking not found
     * @throws IllegalStateException if trip is not completed
     */
    @PostMapping("/{bookingId}/pay")
    public ResponseEntity<TripPayment> createTripPayment(
            @PathVariable String bookingId,
            @Valid @RequestBody TripPayment tripPayment) {

        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        if (!booking.getStatus().equals(TripBooking.BookingStatus.COMPLETED)) {
            throw new IllegalStateException("Only COMPLETED trips can be paid");
        }

        // Validate payment amount matches total fare (fix BigDecimal '-' operation error)
        if (tripPayment.getAmount().subtract(booking.getTotalFare()).abs().compareTo(new BigDecimal("0.01")) > 0) {
            throw new IllegalStateException("Payment amount (" + tripPayment.getAmount() +
                    ") does not match total fare (" + booking.getTotalFare() + ")");
        }

        tripPayment.setTripBooking(booking);
        tripPayment.setPaymentTime(LocalDateTime.now());
        tripPayment.setStatus(TripPayment.PaymentStatus.COMPLETED);

        TripPayment createdPayment = tripPaymentRepository.save(tripPayment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    /**
     * Create trip review (customer rates driver/trip)
     * POST /api/v1/bookings/{bookingId}/review
     * @param bookingId Unique ID of the booking
     * @param tripReview TripReview object with rating and comments
     * @return Created TripReview object with HTTP 201 status
     * @throws EntityNotFoundException if booking not found
     * @throws IllegalStateException if trip is not completed or not paid
     */
    @PostMapping("/{bookingId}/review")
    public ResponseEntity<TripReview> createTripReview(
            @PathVariable String bookingId,
            @Valid @RequestBody TripReview tripReview) {

        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        // Validate trip is completed and paid
        if (!booking.getStatus().equals(TripBooking.BookingStatus.COMPLETED)) {
            throw new IllegalStateException("Only COMPLETED trips can be reviewed");
        }

        // Check if payment exists (fix findByTripBookingBookingId → use original findByTripBooking)
        Optional<TripPayment> payment = tripPaymentRepository.findByTripBooking(booking);
        if (payment.isEmpty() || !payment.get().getStatus().equals(TripPayment.PaymentStatus.COMPLETED)) {
            throw new IllegalStateException("Trip must be paid before review");
        }

        tripReview.setTripBooking(booking);
        tripReview.setCustomer(booking.getCustomer());
        tripReview.setDriver(booking.getDriver());
        // Remove setReviewTime - entity has reviewedAt (auto set)

        // Update driver rating (simple average calculation, fix Integer → Double type error)
        updateDriverRating(tripReview);

        TripReview createdReview = tripReviewRepository.save(tripReview);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * Get booking by ID
     * GET /api/v1/bookings/{bookingId}
     * @param bookingId Unique ID of the booking
     * @return TripBooking object with HTTP 200 status
     * @throws EntityNotFoundException if booking not found
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<TripBooking> getBookingById(@PathVariable String bookingId) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
        return ResponseEntity.ok(booking);
    }

    /**
     * Get bookings by customer ID (filter by status optional)
     * GET /api/v1/bookings/customer/{customerId}
     * @param customerId ID of the customer
     * @param status Optional - booking status filter
     * @return List of TripBooking objects with HTTP 200 status
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TripBooking>> getBookingsByCustomer(
            @PathVariable String customerId,
            @RequestParam(required = false) TripBooking.BookingStatus status) {

        // Fix findByCustomerCustomerIdAndStatus → use original findByCustomerAndStatus
        CustomerProfile customer = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        List<TripBooking> bookings;
        if (status != null) {
            bookings = tripBookingRepository.findByCustomerAndStatus(customer, status);
        } else {
            bookings = tripBookingRepository.findAll().stream()
                    .filter(b -> b.getCustomer().getCustomerId().equals(customerId))
                    .toList();
        }
        return ResponseEntity.ok(bookings);
    }

    /**
     * Get bookings by driver ID (filter by status optional)
     * GET /api/v1/bookings/driver/{driverId}
     * @param driverId ID of the driver
     * @param status Optional - booking status filter
     * @return List of TripBooking objects with HTTP 200 status
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<TripBooking>> getBookingsByDriver(
            @PathVariable String driverId,
            @RequestParam(required = false) TripBooking.BookingStatus status) {

        // Fix findByDriverDriverIdAndStatus + getDriverId() → use original driverProfileId
        DriverProfile driver = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverId));
        List<TripBooking> bookings;
        if (status != null) {
            bookings = tripBookingRepository.findAll().stream()
                    .filter(b -> b.getDriver() != null && b.getDriver().getDriverProfileId().equals(driverId) && b.getStatus().equals(status))
                    .toList();
        } else {
            bookings = tripBookingRepository.findAll().stream()
                    .filter(b -> b.getDriver() != null && b.getDriver().getDriverProfileId().equals(driverId))
                    .toList();
        }
        return ResponseEntity.ok(bookings);
    }

    /**
     * Cancel booking (only PENDING/CONFIRMED bookings)
     * PATCH /api/v1/bookings/{bookingId}/cancel
     * @param bookingId Unique ID of the booking
     * @return Updated TripBooking object with HTTP 200 status
     * @throws EntityNotFoundException if booking not found
     * @throws IllegalStateException if booking is in progress/completed
     */
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<TripBooking> cancelBooking(@PathVariable String bookingId) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        // Fix IN_PROGRESS → original ON_GOING, remove setCancelTime
        if (booking.getStatus().equals(TripBooking.BookingStatus.ON_GOING) ||
                booking.getStatus().equals(TripBooking.BookingStatus.COMPLETED)) {
            throw new IllegalStateException("Cannot cancel ON_GOING or COMPLETED bookings");
        }

        booking.setStatus(TripBooking.BookingStatus.CANCELLED);

        // Restore driver/cab status if confirmed
        if (booking.getStatus().equals(TripBooking.BookingStatus.CONFIRMED)) {
            if (booking.getDriver() != null) {
                DriverProfile driver = booking.getDriver();
                driver.setCurrentStatus(DriverProfile.DriverStatus.ONLINE);
                driverProfileRepository.save(driver);
            }
            if (booking.getCab() != null) {
                Cab cab = booking.getCab();
                cab.setStatus(Cab.CabStatus.AVAILABLE);
                cabRepository.save(cab);
            }
        }

        TripBooking updatedBooking = tripBookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Helper method to update driver rating (simple average calculation)
     * Fix Integer rating → Double type conversion error
     * @param review TripReview object with customer rating
     */
    private void updateDriverRating(TripReview review) {
        DriverProfile driver = review.getDriver();
        double newRating = review.getRating() != null ? review.getRating().doubleValue() : 0.0;
        if (driver.getRating() == null || driver.getRating() == 0.0) {
            driver.setRating(newRating);
        } else {
            // Simple average: (current rating + new rating) / 2
            driver.setRating((driver.getRating() + newRating) / 2.0);
        }
        driverProfileRepository.save(driver);
    }
}