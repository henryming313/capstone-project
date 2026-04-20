package com.centria.cabbookingmvp.service.impl;

import com.centria.cabbookingmvp.entity.*;
import com.centria.cabbookingmvp.repository.TripBookingRepository;
import com.centria.cabbookingmvp.repository.TripRejectionRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import com.centria.cabbookingmvp.service.TripRejectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripRejectServiceImpl implements TripRejectService {

    private final TripBookingRepository tripRepo;
    private final TripRejectionRepository rejectRepo;
    private final UserRepository userRepo;

    public TripRejectServiceImpl(TripBookingRepository tripRepo, TripRejectionRepository rejectRepo, UserRepository userRepo) {
        this.tripRepo = tripRepo;
        this.rejectRepo = rejectRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void rejectTrip(Long tripId, Long driverId) {
        if (tripId == null) throw new IllegalArgumentException("tripId is required");
        if (driverId == null) throw new IllegalArgumentException("driverId is required");

        User driver = userRepo.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
        if (!"DRIVER".equalsIgnoreCase(driver.getRole())) {
            throw new IllegalArgumentException("User is not a DRIVER");
        }

        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));

        if (trip.getStatus() != TripStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING trips can be rejected");
        }

        if (rejectRepo.existsByTrip_IdAndDriver_Id(tripId, driverId)) {
            throw new IllegalArgumentException("Trip already rejected by this driver");
        }

        TripRejection r = new TripRejection();
        r.setTrip(trip);
        r.setDriver(driver);
        rejectRepo.save(r);
    }

    @Override
    public List<TripBooking> listPendingTripsForDriver(Long driverId) {
        if (driverId == null) throw new IllegalArgumentException("driverId is required");
        return tripRepo.findPendingTripsExcludingRejected(driverId);
    }
}
