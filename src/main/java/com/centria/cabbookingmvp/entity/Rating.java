package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Instant;

@Entity
@Table(
        name = "ratings",
        uniqueConstraints = @UniqueConstraint(name = "uk_rating_trip", columnNames = "trip_id"),
        indexes = {
                @Index(name = "idx_rating_driver", columnList = "driver_id"),
                @Index(name = "idx_rating_rider", columnList = "rider_id")
        }
)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one trip -> one rating (MVP)
    @OneToOne(optional = false)
    @JoinColumn(name = "trip_id", foreignKey = @ForeignKey(name = "fk_rating_trip"))
    private TripBooking trip;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rider_id", foreignKey = @ForeignKey(name = "fk_rating_rider"))
    private User rider;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", foreignKey = @ForeignKey(name = "fk_rating_driver"))
    private User driver;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private int score;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }

    public TripBooking getTrip() { return trip; }
    public void setTrip(TripBooking trip) { this.trip = trip; }

    public User getRider() { return rider; }
    public void setRider(User rider) { this.rider = rider; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Instant getCreatedAt() { return createdAt; }
}
