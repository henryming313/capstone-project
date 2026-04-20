package com.centria.cabbookingmvp.controller.dto;

import java.math.BigDecimal;

public class FareEstimateResponse {

    private BigDecimal baseFare;
    private BigDecimal routeFare;
    private BigDecimal estimatedTotal;
    private String currency;

    public FareEstimateResponse() {}

    public FareEstimateResponse(BigDecimal baseFare, BigDecimal routeFare, BigDecimal estimatedTotal, String currency) {
        this.baseFare = baseFare;
        this.routeFare = routeFare;
        this.estimatedTotal = estimatedTotal;
        this.currency = currency;
    }

    public BigDecimal getBaseFare() { return baseFare; }
    public void setBaseFare(BigDecimal baseFare) { this.baseFare = baseFare; }

    public BigDecimal getRouteFare() { return routeFare; }
    public void setRouteFare(BigDecimal routeFare) { this.routeFare = routeFare; }

    public BigDecimal getEstimatedTotal() { return estimatedTotal; }
    public void setEstimatedTotal(BigDecimal estimatedTotal) { this.estimatedTotal = estimatedTotal; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
