package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "trip_rejections",
        uniqueConstraints = @UniqueConstraint(name = "uk_reject_trip_driver", columnNames = {"trip_id", "driver_id"}),
        indexes = {
                @Index(name = "idx_reject_driver", columnList = "driver_id"),
                @Index(name = "idx_reject_trip", columnList = "trip_id")
        }
)
public class TripRejection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id", foreignKey = @ForeignKey(name = "fk_reject_trip"))
    private TripBooking trip;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", foreignKey = @ForeignKey(name = "fk_reject_driver"))
    private User driver;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }

    public TripBooking getTrip() { return trip; }
    public void setTrip(TripBooking trip) { this.trip = trip; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public Instant getCreatedAt() { return createdAt; }
}
