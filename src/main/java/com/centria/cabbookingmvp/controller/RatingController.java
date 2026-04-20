package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.controller.dto.CreateRatingRequest;
import com.centria.cabbookingmvp.entity.Rating;
import com.centria.cabbookingmvp.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ApiResponse<Rating> create(@Valid @RequestBody CreateRatingRequest req) {
        Rating rating = ratingService.createRating(req.getTripId(), req.getRiderId(), req.getScore(), req.getComment());
        return ApiResponse.success(rating);
    }

    @GetMapping("/trip/{tripId}")
    public ApiResponse<Rating> getByTrip(@PathVariable Long tripId) {
        return ApiResponse.success(ratingService.getByTrip(tripId));
    }

    @GetMapping("/driver/{driverId}")
    public ApiResponse<List<Rating>> listByDriver(@PathVariable Long driverId) {
        return ApiResponse.success(ratingService.listByDriver(driverId));
    }
}
