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

import java.util.List;

@Service
public class TripBookingService {

    private final TripBookingRepository tripRepo;
    private final UserRepository userRepo;
    private final CabRepository cabRepo;

    public TripBookingService(TripBookingRepository tripRepo,
                              UserRepository userRepo,
                              CabRepository cabRepo) {
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
        this.cabRepo = cabRepo;
    }

    // 1. 创建订单
    public TripBooking createTrip(CreateTripRequest req) {
        User rider = userRepo.findById(req.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        TripBooking trip = new TripBooking();
        trip.setRider(rider);
        trip.setPickupLocation(req.getPickupLocation());
        trip.setDropoffLocation(req.getDropoffLocation());
        trip.setStatus(TripStatus.PENDING);

        return tripRepo.save(trip);
    }

    // 2. 司机接单
    public TripBooking acceptTrip(Long tripId, AcceptTripRequest req) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.PENDING) {
            throw new RuntimeException("Only PENDING trip can be accepted");
        }

        User driver = userRepo.findById(req.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Cab cab = cabRepo.findById(req.getCabId())
                .orElseThrow(() -> new RuntimeException("Cab not found"));

        trip.setDriver(driver);
        trip.setCab(cab);
        trip.setStatus(TripStatus.ACCEPTED);

        return tripRepo.save(trip);
    }

    // 3. 开始行程
    public TripBooking startTrip(Long tripId) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.ACCEPTED) {
            throw new RuntimeException("Only ACCEPTED trip can be started");
        }

        trip.setStatus(TripStatus.IN_PROGRESS);
        return tripRepo.save(trip);
    }

    // 4. 完成行程
    public TripBooking completeTrip(Long tripId) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new RuntimeException("Only IN_PROGRESS trip can be completed");
        }

        trip.setStatus(TripStatus.COMPLETED);
        return tripRepo.save(trip);
    }

    // 5. 取消订单
    public TripBooking cancelTrip(Long tripId) {
        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new RuntimeException("Completed trip cannot be cancelled");
        }

        trip.setStatus(TripStatus.CANCELLED);
        return tripRepo.save(trip);
    }

    // 6. 乘客查自己的订单
    public List<TripBooking> getTripsByPassenger(Long riderId) {
        return tripRepo.findByRider_Id(riderId);
    }

    // 7. 司机查自己的订单
    public List<TripBooking> getTripsByDriver(Long driverId) {
        return tripRepo.findByDriver_Id(driverId);
    }

    // 8. 按状态查订单
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
