package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.entity.*;
import com.centria.cabbookingmvp.entity.UserStatus;
import com.centria.cabbookingmvp.entity.TripStatus;
import com.centria.cabbookingmvp.service.AdminService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ----- Users -----
    @GetMapping("/users")
    public ApiResponse<List<User>> listUsers(@RequestParam Long adminId) {
        return ApiResponse.success(adminService.listUsers(adminId));
    }

    @PutMapping("/users/{userId}/status")
    public ApiResponse<User> updateUserStatus(
            @RequestParam Long adminId,
            @PathVariable Long userId,
            @RequestParam UserStatus status
    ) {
        return ApiResponse.success(adminService.updateUserStatus(adminId, userId, status));
    }

    // ----- Cabs -----
    @GetMapping("/cabs")
    public ApiResponse<List<Cab>> listCabs(@RequestParam Long adminId) {
        return ApiResponse.success(adminService.listCabs(adminId));
    }

    @PutMapping("/cabs/{cabId}/active")
    public ApiResponse<Cab> setCabActive(
            @RequestParam Long adminId,
            @PathVariable Long cabId,
            @RequestParam boolean active
    ) {
        return ApiResponse.success(adminService.setCabActive(adminId, cabId, active));
    }

    // ----- Trips -----
    @GetMapping("/trips")
    public ApiResponse<List<TripBooking>> listTrips(
            @RequestParam Long adminId,
            @RequestParam(required = false) TripStatus status
    ) {
        return ApiResponse.success(adminService.listTrips(adminId, status));
    }

    @GetMapping("/trips/{tripId}")
    public ApiResponse<TripBooking> getTrip(@RequestParam Long adminId, @PathVariable Long tripId) {
        return ApiResponse.success(adminService.getTrip(adminId, tripId));
    }

    // ----- Assign -----
    @PutMapping("/trips/{tripId}/assign")
    public ApiResponse<TripBooking> assignTrip(
            @RequestParam Long adminId,
            @PathVariable Long tripId,
            @RequestParam Long driverId,
            @RequestParam Long cabId
    ) {
        return ApiResponse.success(adminService.assignTrip(adminId, tripId, driverId, cabId));
    }
}
