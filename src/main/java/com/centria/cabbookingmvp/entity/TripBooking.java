package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "trip_bookings", indexes = {
        @Index(name = "idx_trip_rider", columnList = "rider_id"),
        @Index(name = "idx_trip_driver", columnList = "driver_id"),
        @Index(name = "idx_trip_status", columnList = "status")
})
public class TripBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 乘客（必填）
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "rider_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_trip_rider"))
    private User rider;

    // 司机（可为空：未派单）
    @ManyToOne
    @JoinColumn(name = "driver_id",
            foreignKey = @ForeignKey(name = "fk_trip_driver"))
    private User driver;

    // 车辆（可为空：未派车）
    @ManyToOne
    @JoinColumn(name = "cab_id",
            foreignKey = @ForeignKey(name = "fk_trip_cab"))
    private Cab cab;

    // 简化：先用文本地址（后续你再升级成 lat/lng）
    @Column(nullable = false, length = 255)
    private String pickupLocation;

    @Column(nullable = false, length = 255)
    private String dropoffLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripStatus status = TripStatus.REQUESTED;

    // 费用：MVP 先保留一个 totalFare
    @Column(precision = 12, scale = 2)
    private BigDecimal totalFare;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public TripBooking() {}

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

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