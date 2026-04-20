package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.controller.dto.FareEstimateResponse;

public interface FareEstimateService {
    FareEstimateResponse estimate(String pickupLocation, String dropoffLocation);
}
