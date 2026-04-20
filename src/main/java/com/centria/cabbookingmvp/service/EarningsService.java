package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.controller.dto.DriverEarningsResponse;

public interface EarningsService {
    DriverEarningsResponse getDriverEarnings(Long driverId);
}
