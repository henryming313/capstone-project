package com.centria.cabbookingmvp.controller.dto;

import jakarta.validation.constraints.NotNull;

public class AcceptTripRequest {

    @NotNull
    private Long driverId;

    @NotNull
    private Long cabId;

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public Long getCabId() { return cabId; }
    public void setCabId(Long cabId) { this.cabId = cabId; }
}
