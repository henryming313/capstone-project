package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing trip payment records
 * Maps to database table "trip_payments"
 */
@Entity
@Table(name = "trip_payments")
@Data
public class TripPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId; // Unique identifier for payment record

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private TripBooking tripBooking; // Associated trip booking

    @Column(nullable = false)
    private BigDecimal amount; // Payment amount

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // Payment method type

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING; // Payment status (default: PENDING)

    private LocalDateTime paymentTime; // Time when payment was processed
    private String transactionId; // External transaction ID from payment gateway

    /**
     * Enum representing available payment methods
     */
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, UPI, CASH, WALLET
    }

    /**
     * Enum representing payment statuses
     */
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
}