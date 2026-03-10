package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

// Specify the database table name as users
@Entity

// Specify the database table name as trip_bookings
// Also create a database index to improve query speed
@Table(name = "trip_bookings", indexes = {
        @Index(name = "idx_trip_rider", columnList = "rider_id"),// rider
        @Index(name = "idx_trip_driver", columnList = "driver_id"),// driver
        @Index(name = "idx_trip_status", columnList = "status")// status
})
public class TripBooking {

    @Id // id is the database primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id is the database primary key
    private Long id;

    // rider
    @NotNull
    // A user can have many orders
    @ManyToOne(optional = false)
    // This field corresponds to the database column rider_id
    @JoinColumn(name = "rider_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_trip_rider"))
    private User rider;

    // Driver
    @ManyToOne
    // This field corresponds to the database column driver_id
    @JoinColumn(name = "driver_id",
            foreignKey = @ForeignKey(name = "fk_trip_driver"))
    private User driver;

    // car
    @ManyToOne
    // This field corresponds to the database column cab_id
    @JoinColumn(name = "cab_id",
            foreignKey = @ForeignKey(name = "fk_trip_cab"))
    private Cab cab;

    // Pick-up location
    @NotNull
    @Column(nullable = false, length = 255)
    private String pickupLocation;

    // Drop-off location
    @NotNull
    @Column(nullable = false, length = 255)
    private String dropoffLocation;
    // Order status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripStatus status = TripStatus.PENDING;

    // total price
    @Column(precision = 12, scale = 2)
    private BigDecimal totalFare;
    // Order creation time
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    // Order update time
    @Column(nullable = false)
    private Instant updatedAt;

    public TripBooking() {}

    // Execute automatically before data is saved to the database

    // Automatically set creation and update times
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
    // Automatically update updatedAt
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // ===== getters/setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getRider() { return rider; }
    public void setRider(User rider) { this.rider = rider; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public Cab getCab() { return cab; }
    public void setCab(Cab cab) { this.cab = cab; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public BigDecimal getTotalFare() { return totalFare; }
    public void setTotalFare(BigDecimal totalFare) { this.totalFare = totalFare; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}