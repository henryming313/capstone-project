package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.entity.TripBooking;
import com.centria.cabbookingmvp.service.TripRejectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripRejectController {

    private final TripRejectService tripRejectService;

    public TripRejectController(TripRejectService tripRejectService) {
        this.tripRejectService = tripRejectService;
    }

    // driver reject a pending trip (does not change trip status)
    @PutMapping("/{tripId}/reject")
    public ApiResponse<String> reject(@PathVariable Long tripId, @RequestParam Long driverId) {
        tripRejectService.rejectTrip(tripId, driverId);
        return ApiResponse.success("rejected");
    }

    // pending trips for driver excluding already rejected ones
    @GetMapping("/pending")
    public ApiResponse<List<TripBooking>> pendingForDriver(@RequestParam Long driverId) {
        return ApiResponse.success(tripRejectService.listPendingTripsForDriver(driverId));
    }
}