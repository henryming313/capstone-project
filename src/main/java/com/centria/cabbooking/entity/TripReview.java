package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_reviews")
@Data
public class TripReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reviewId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private TripBooking tripBooking;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerProfile customer;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverProfile driver;

    private Integer rating;
    private String comments;
    private LocalDateTime reviewedAt = LocalDateTime.now();
}