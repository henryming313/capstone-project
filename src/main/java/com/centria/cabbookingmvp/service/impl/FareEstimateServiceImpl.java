package com.centria.cabbookingmvp.service.impl;

import com.centria.cabbookingmvp.controller.dto.FareEstimateResponse;
import com.centria.cabbookingmvp.service.FareEstimateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class FareEstimateServiceImpl implements FareEstimateService {

    private static final BigDecimal BASE_FARE = new BigDecimal("5.00");
    private static final String CURRENCY = "EUR";

    // mock route pricing: "pickup->dropoff" -> routeFare
    private static final Map<String, BigDecimal> ROUTE_PRICE = new HashMap<>();
    static {
        ROUTE_PRICE.put(key("Centria University", "Kokkola Railway Station"), new BigDecimal("8.00"));
        ROUTE_PRICE.put(key("Kokkola Railway Station", "Centria University"), new BigDecimal("8.00"));
        ROUTE_PRICE.put(key("City Center", "Campus"), new BigDecimal("6.00"));
        ROUTE_PRICE.put(key("Campus", "City Center"), new BigDecimal("6.00"));

    }

    @Override
    public FareEstimateResponse estimate(String pickupLocation, String dropoffLocation) {
        if (pickupLocation == null || pickupLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("pickupLocation is required");
        }
        if (dropoffLocation == null || dropoffLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("dropoffLocation is required");
        }
        String k = key(pickupLocation.trim(), dropoffLocation.trim());
        BigDecimal routeFare = ROUTE_PRICE.getOrDefault(k, new BigDecimal("7.00")); // default mock
        BigDecimal total = BASE_FARE.add(routeFare);

        return new FareEstimateResponse(BASE_FARE, routeFare, total, CURRENCY);
    }

    private static String key(String a, String b) {
        return a + "->" + b;
    }
}
