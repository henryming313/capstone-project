package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_bookings")
@Data
public class TripBooking {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String bookingId;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerProfile customer;

	@ManyToOne
	@JoinColumn(name = "driver_id")
	private DriverProfile driver;

	@ManyToOne
	@JoinColumn(name = "cab_id")
	private Cab cab;

	@Column(nullable = false)
	private String pickupLocation;

	@Column(nullable = false)
	private String dropoffLocation;

	private Double distanceKm;

	@Column(nullable = false)
	private BigDecimal baseFare;

	private BigDecimal surgeMultiplier = BigDecimal.ONE;

	private BigDecimal totalFare;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;

	public enum BookingStatus {
		PENDING, CONFIRMED, ON_GOING, COMPLETED, CANCELLED
	}
}