package com.centria.cabbookingmvp.controller.dto;

import java.math.BigDecimal;

public class DriverEarningsResponse {

    private Long driverId;
    private long completedTrips;
    private BigDecimal totalEarnings;
    private BigDecimal averageFare;
    private String currency;

    public DriverEarningsResponse() {}

    public DriverEarningsResponse(Long driverId, long completedTrips, BigDecimal totalEarnings, BigDecimal averageFare, String currency) {
        this.driverId = driverId;
        this.completedTrips = completedTrips;
        this.totalEarnings = totalEarnings;
        this.averageFare = averageFare;
        this.currency = currency;
    }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public long getCompletedTrips() { return completedTrips; }
    public void setCompletedTrips(long completedTrips) { this.completedTrips = completedTrips; }

    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(BigDecimal totalEarnings) { this.totalEarnings = totalEarnings; }

    public BigDecimal getAverageFare() { return averageFare; }
    public void setAverageFare(BigDecimal averageFare) { this.averageFare = averageFare; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
