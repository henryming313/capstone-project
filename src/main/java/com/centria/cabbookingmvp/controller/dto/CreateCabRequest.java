package com.centria.cabbookingmvp.controller.dto;

import com.centria.cabbookingmvp.entity.CabType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCabRequest {

    @NotBlank
    @Size(max = 32)
    private String plateNumber;

    @Size(max = 64)
    private String brand;

    @Size(max = 64)
    private String model;

    @Size(max = 32)
    private String color;

    @NotNull
    private CabType cabType;

    @NotNull
    private Long driverId;

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
}
