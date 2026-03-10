package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "cabs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cabs_plate", columnNames = "plateNumber")
        }
)
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String plateNumber;

    @Size(max = 64)
    @Column(length = 64)
    private String brand;

    @Size(max = 64)
    @Column(length = 64)
    private String model;

    @Size(max = 32)
    @Column(length = 32)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private CabType cabType = CabType.SEDAN;

    @NotNull
    @Column(nullable = false)
    private Long driverId;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private CabStatus status = CabStatus.OFFLINE;

    public Cab() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public CabType getCabType() { return cabType; }
    public void setCabType(CabType cabType) { this.cabType = cabType; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public CabStatus getStatus() { return status; }
    public void setStatus(CabStatus status) { this.status = status; }
}