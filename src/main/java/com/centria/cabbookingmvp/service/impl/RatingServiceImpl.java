package com.centria.cabbookingmvp.service.impl;

import com.centria.cabbookingmvp.entity.*;
import com.centria.cabbookingmvp.repository.RatingRepository;
import com.centria.cabbookingmvp.repository.TripBookingRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import com.centria.cabbookingmvp.service.RatingService;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepo;
    private final TripBookingRepository tripRepo;
    private final UserRepository userRepo;

    public RatingServiceImpl(RatingRepository ratingRepo, TripBookingRepository tripRepo, UserRepository userRepo) {
        this.ratingRepo = ratingRepo;
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Rating createRating(Long tripId, Long riderId, int score, String comment) {

        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));

        if (trip.getStatus() != TripStatus.COMPLETED) {
            throw new IllegalArgumentException("Only COMPLETED trips can be rated");
        }

        User rider = userRepo.findById(riderId)
                .orElseThrow(() -> new IllegalArgumentException("Rider not found: " + riderId));

        if (!"RIDER".equalsIgnoreCase(rider.getRole())) {
            throw new IllegalArgumentException("Only RIDER can rate a trip");
        }

        if (trip.getRider() == null || !trip.getRider().getId().equals(riderId)) {
            throw new IllegalArgumentException("This trip does not belong to the rider");
        }

        if (trip.getDriver() == null) {
            throw new IllegalArgumentException("Trip has no driver");
        }

        ratingRepo.findByTrip_Id(tripId).ifPresent(r -> {
            throw new IllegalArgumentException("This trip is already rated");
        });

        Rating rating = new Rating();
        rating.setTrip(trip);
        rating.setRider(rider);
        rating.setDriver(trip.getDriver());
        rating.setScore(score);
        rating.setComment(comment);

        return ratingRepo.save(rating);
    }

    @Override
    public Rating getByTrip(Long tripId) {
        return ratingRepo.findByTrip_Id(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found for trip: " + tripId));
    }

    @Override
    public java.util.List<Rating> listByDriver(Long driverId) {
        return ratingRepo.findByDriver_Id(driverId);
    }
}
