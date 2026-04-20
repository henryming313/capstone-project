package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.entity.Rating;

import java.util.List;

public interface RatingService {
    Rating createRating(Long tripId, Long riderId, int score, String comment);
    Rating getByTrip(Long tripId);
    List<Rating> listByDriver(Long driverId);
}
