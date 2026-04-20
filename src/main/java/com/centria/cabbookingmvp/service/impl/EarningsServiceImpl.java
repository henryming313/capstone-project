package com.centria.cabbookingmvp.service.impl;

import com.centria.cabbookingmvp.controller.dto.DriverEarningsResponse;
import com.centria.cabbookingmvp.entity.TripStatus;
import com.centria.cabbookingmvp.entity.User;
import com.centria.cabbookingmvp.repository.TripBookingRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import com.centria.cabbookingmvp.service.EarningsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EarningsServiceImpl implements EarningsService {

    private final TripBookingRepository tripRepo;
    private final UserRepository userRepo;

    public EarningsServiceImpl(TripBookingRepository tripRepo, UserRepository userRepo) {
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
    }

    @Override
    public DriverEarningsResponse getDriverEarnings(Long driverId) {
        if (driverId == null) throw new IllegalArgumentException("driverId is required");

        User driver = userRepo.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        if (!"DRIVER".equalsIgnoreCase(driver.getRole())) {
            throw new IllegalArgumentException("User is not a DRIVER");
        }

        long completed = tripRepo.countByDriver_IdAndStatus(driverId, TripStatus.COMPLETED);
        BigDecimal total = tripRepo.sumCompletedFareByDriver(driverId);

        BigDecimal avg = BigDecimal.ZERO;
        if (completed > 0) {
            avg = total.divide(new BigDecimal(completed), 2, RoundingMode.HALF_UP);
        }

        return new DriverEarningsResponse(driverId, completed, total, avg, "EUR");
    }
}
