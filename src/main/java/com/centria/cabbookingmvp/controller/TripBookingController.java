package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.AcceptTripRequest;
import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.controller.dto.CreateTripRequest;
import com.centria.cabbookingmvp.entity.TripBooking;
import com.centria.cabbookingmvp.service.TripBookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripBookingController {

    private final TripBookingService tripService;

    public TripBookingController(TripBookingService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ApiResponse<?> createTrip(@Valid @RequestBody CreateTripRequest req) {
        TripBooking trip = tripService.createTrip(req);
        return ApiResponse.success(trip);
    }

    @PutMapping("/{id}/accept")
    public ApiResponse<?> acceptTrip(@PathVariable Long id,
                                     @Valid @RequestBody AcceptTripRequest req) {
        TripBooking trip = tripService.acceptTrip(id, req);
        return ApiResponse.success(trip);
    }

    @PutMapping("/{id}/start")
    public ApiResponse<?> startTrip(@PathVariable Long id) {
        TripBooking trip = tripService.startTrip(id);
        return ApiResponse.success(trip);
    }

    @PutMapping("/{id}/complete")
    public ApiResponse<?> completeTrip(@PathVariable Long id) {
        TripBooking trip = tripService.completeTrip(id);
        return ApiResponse.success(trip);
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<?> cancelTrip(@PathVariable Long id) {
        TripBooking trip = tripService.cancelTrip(id);
        return ApiResponse.success(trip);
    }

    @GetMapping("/passenger/{userId}")
    public ApiResponse<?> getTripsByPassenger(@PathVariable Long userId) {
        return ApiResponse.success(tripService.getTripsByPassenger(userId));
    }

    @GetMapping("/driver/{driverId}")
    public ApiResponse<?> getTripsByDriver(@PathVariable Long driverId) {
        return ApiResponse.success(tripService.getTripsByDriver(driverId));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<?> getTripsByStatus(@PathVariable String status) {
        return ApiResponse.success(tripService.getTripsByStatus(status));
    }
}
