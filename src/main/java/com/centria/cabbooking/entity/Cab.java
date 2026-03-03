package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "cabs")
@Data
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cabId;

    @Column(unique = true, nullable = false)
    private String carNumber;

    private String carModel;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private BigDecimal perKmRate;

    @Enumerated(EnumType.STRING)
    private CabStatus status = CabStatus.AVAILABLE;

    public enum CarType {
        ECONOMY, PREMIUM, LUXURY
    }

    public enum CabStatus {
        AVAILABLE, ON_TRIP, MAINTENANCE
    }
}