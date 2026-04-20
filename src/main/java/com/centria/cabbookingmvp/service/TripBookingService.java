package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.controller.dto.AcceptTripRequest;
import com.centria.cabbookingmvp.controller.dto.CreateTripRequest;
import com.centria.cabbookingmvp.entity.Cab;
import com.centria.cabbookingmvp.entity.TripBooking;
import com.centria.cabbookingmvp.entity.TripStatus;
import com.centria.cabbookingmvp.entity.User;
import com.centria.cabbookingmvp.repository.CabRepository;
import com.centria.cabbookingmvp.repository.TripBookingRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.centria.cabbookingmvp.service.FareEstimateService;

import java.util.List;

@Service
public class TripBookingService {

    private final TripBookingRepository tripRepo;
    private final UserRepository userRepo;
    private final CabRepository cabRepo;
    private final FareEstimateService fareService;

    public TripBookingService(TripBookingRepository tripRepo,
                              UserRepository userRepo,
                              CabRepository cabRepo,
                              FareEstimateService fareService) {
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
        this.cabRepo = cabRepo;
        this.fareService = fareService;
    }

    // 1. 
    public TripBooking createTrip(CreateTripRequest req) {
        User rider = userRepo.findById(req.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found"));
        if (!"RIDER".equals(rider.getRole())) {
            throw new RuntimeException("Only rider can create trip");
        }

        TripBooking trip = new TripBooking();
        trip.setRider(rider);
        trip.setPickupLocation(req.getPickupLocation());
        trip.setDropoffLocation(req.getDropoffLocation());
        trip.setStatus(TripStatus.PENDING);

        return tripRepo.save(trip);
    }

    // 2. 
    public TripBooking acceptTrip(Long tripId, AcceptTripRequest req) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        User driver = userRepo.findById(req.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));


        if (trip.getStatus() != TripStatus.PENDING) {
            throw new RuntimeException("Only PENDING trip can be accepted");
        }
        if (!"DRIVER".equals(driver.getRole())) {
            throw new RuntimeException("Only driver can accept trip");
        }

        Cab cab = cabRepo.findById(req.getCabId())
                .orElseThrow(() -> new RuntimeException("Cab not found"));
        if (!req.getDriverId().equals(cab.getDriverId())) {
            throw new RuntimeException("Cab does not belong to driver");
        }


        trip.setDriver(driver);
        trip.setCab(cab);
        trip.setStatus(TripStatus.ACCEPTED);

        return tripRepo.save(trip);
    }

    // 3. 
    public TripBooking startTrip(Long tripId) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.ACCEPTED) {
            throw new RuntimeException("Only ACCEPTED trip can be started");
        }

        trip.setStatus(TripStatus.IN_PROGRESS);
        return tripRepo.save(trip);
    }

    public TripBooking completeTrip(Long tripId) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new RuntimeException("Only IN_PROGRESS trip can be completed");
        }

        //  calculate prize
        var estimate = fareService.estimate(
                trip.getPickupLocation(),
                trip.getDropoffLocation()
        );

        trip.setTotalFare(estimate.getEstimatedTotal());

        trip.setStatus(TripStatus.COMPLETED);

        return tripRepo.save(trip);
    }

    public TripBooking cancelTrip(Long tripId, Long userId) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.PENDING && trip.getStatus() != TripStatus.ACCEPTED) {
            throw new RuntimeException("Trip cannot be cancelled");
        }

        if (trip.getRider() == null || !trip.getRider().getId().equals(userId)) {
            throw new RuntimeException("Only rider can cancel trip");
        }

        trip.setStatus(TripStatus.CANCELLED);
        return tripRepo.save(trip);
    }

    // 6. 
    public List<TripBooking> getTripsByPassenger(Long riderId) {
        return tripRepo.findByRider_Id(riderId);
    }
    
    // 7. 
    public List<TripBooking> getTripsByDriver(Long driverId) {
        return tripRepo.findByDriver_Id(driverId);
    }
    
    // 8. 
    public List<TripBooking> getTripsByStatus(String statusText) {
        TripStatus status;
        try {
            status = TripStatus.valueOf(statusText.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid trip status");
        }

        return tripRepo.findByStatus(status);
    }
}
