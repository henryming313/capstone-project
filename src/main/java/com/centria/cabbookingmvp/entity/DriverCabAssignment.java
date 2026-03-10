package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "driver_cab_assignment", indexes = {
        @Index(name = "idx_assign_driver", columnList = "driver_id"),
        @Index(name = "idx_assign_cab", columnList = "cab_id")
})
public class DriverCabAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_assign_driver"))
    private User driver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cab_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_assign_cab"))
    private Cab cab;

    @Column(nullable = false, updatable = false)
    private Instant startTime;

    @Column
    private Instant endTime;

    @Column(nullable = false)
    private boolean current = true;

    @PrePersist
    protected void onCreate() {
        this.startTime = Instant.now();
    }

    public DriverCabAssignment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public Cab getCab() { return cab; }
    public void setCab(Cab cab) { this.cab = cab; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }

    public boolean isCurrent() { return current; }
    public void setCurrent(boolean current) { this.current = current; }
}
