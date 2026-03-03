package com.centria.cabbooking.service;

import com.centria.cabbooking.entity.Cab;
import com.centria.cabbooking.entity.CustomerProfile;
import com.centria.cabbooking.entity.DriverProfile;
import com.centria.cabbooking.entity.TripBooking;
import com.centria.cabbooking.repository.CabRepository;
import com.centria.cabbooking.repository.CustomerProfileRepository;
import com.centria.cabbooking.repository.DriverProfileRepository;
import com.centria.cabbooking.repository.TripBookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of TripBookingService interface
 * Handles core trip booking business logic
 */
@Service
@RequiredArgsConstructor
public class TripBookingServiceImpl implements TripBookingService {

    private final TripBookingRepository tripBookingRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CabRepository cabRepository;

    @Override
    @Transactional
    public TripBooking createBooking(TripBooking tripBooking) {
        CustomerProfile customer = customerProfileRepository.findById(tripBooking.getCustomer().getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        tripBooking.setCustomer(customer);

        tripBooking.setStatus(TripBooking.BookingStatus.PENDING);
        tripBooking.setTotalFare(calculateFare(tripBooking.getBaseFare(), 0.0, tripBooking.getSurgeMultiplier()));

        return tripBookingRepository.save(tripBooking);
    }

    @Override
    @Transactional
    public TripBooking assignDriverAndCab(String bookingId, String driverId, String cabId) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != TripBooking.BookingStatus.PENDING) {
            throw new IllegalStateException("Cannot assign driver to non-pending booking");
        }

        DriverProfile driver = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverId));

        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found with ID: " + cabId));

        booking.setDriver(driver);
        booking.setCab(cab);
        booking.setStatus(TripBooking.BookingStatus.CONFIRMED);

        driver.setCurrentStatus(DriverProfile.DriverStatus.ON_TRIP);
        cab.setStatus(Cab.CabStatus.ON_TRIP);

        driverProfileRepository.save(driver);
        cabRepository.save(cab);

        return tripBookingRepository.save(booking);
    }

    @Override
    @Transactional
    public TripBooking startTrip(String bookingId) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != TripBooking.BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot start non-confirmed booking");
        }

        booking.setStatus(TripBooking.BookingStatus.ON_GOING);
        booking.setStartedAt(LocalDateTime.now());

        return tripBookingRepository.save(booking);
    }

    @Override
    @Transactional
    public TripBooking completeTrip(String bookingId, Double distanceKm) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != TripBooking.BookingStatus.ON_GOING) {
            throw new IllegalStateException("Cannot complete non-on-going booking");
        }

        booking.setDistanceKm(distanceKm);
        booking.setTotalFare(calculateFare(booking.getBaseFare(), distanceKm, booking.getSurgeMultiplier(), booking.getCab()));
        booking.setStatus(TripBooking.BookingStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now());

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

        return tripBookingRepository.save(booking);
    }

    @Override
    @Transactional
    public TripBooking cancelBooking(String bookingId) {
        TripBooking booking = tripBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == TripBooking.BookingStatus.ON_GOING ||
                booking.getStatus() == TripBooking.BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel on-going/completed booking");
        }

        TripBooking.BookingStatus originalStatus = booking.getStatus();
        booking.setStatus(TripBooking.BookingStatus.CANCELLED);

        if (originalStatus == TripBooking.BookingStatus.CONFIRMED && booking.getDriver() != null) {
            DriverProfile driver = booking.getDriver();
            driver.setCurrentStatus(DriverProfile.DriverStatus.ONLINE);
            driverProfileRepository.save(driver);

            Cab cab = booking.getCab();
            cab.setStatus(Cab.CabStatus.AVAILABLE);
            cabRepository.save(cab);
        }

        return tripBookingRepository.save(booking);
    }

    @Override
    public BigDecimal calculateFare(BigDecimal baseFare, Double distanceKm, BigDecimal surgeMultiplier) {
        return calculateFare(baseFare, distanceKm, surgeMultiplier, null);
    }

    private BigDecimal calculateFare(BigDecimal baseFare, Double distanceKm, BigDecimal surgeMultiplier, Cab cab) {
        if (distanceKm == null || distanceKm <= 0) {
            return baseFare.multiply(surgeMultiplier).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal perKmRate = (cab != null && cab.getPerKmRate() != null) ? cab.getPerKmRate() : BigDecimal.valueOf(10.0);
        BigDecimal distanceFare = perKmRate.multiply(BigDecimal.valueOf(distanceKm));
        BigDecimal totalFare = (baseFare.add(distanceFare)).multiply(surgeMultiplier);

        return totalFare.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripBooking> getCustomerBookings(String customerId) {
        CustomerProfile customer = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        return tripBookingRepository.findByCustomerAndStatus(customer, TripBooking.BookingStatus.COMPLETED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripBooking> getDriverBookings(String driverId, LocalDateTime startDate, LocalDateTime endDate) {
        DriverProfile driver = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverId));

        return tripBookingRepository.findByDriverAndCreatedAtBetween(driver, startDate, endDate);
    }
}