package com.centria.cabbookingmvp.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class FareEstimateRequest {

    @NotBlank
    private String pickupLocation;

    @NotBlank
    private String dropoffLocation;

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
}
