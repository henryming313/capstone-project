package com.centria.cabbooking.repository;

import com.centria.cabbooking.entity.TripPayment;
import com.centria.cabbooking.entity.TripBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for TripPayment entity operations
 */
@Repository
public interface TripPaymentRepository extends JpaRepository<TripPayment, String> {
    // Find payment by booking
    Optional<TripPayment> findByTripBooking(TripBooking tripBooking);

    // Find payments by transaction ID
    Optional<TripPayment> findByTransactionId(String transactionId);
}