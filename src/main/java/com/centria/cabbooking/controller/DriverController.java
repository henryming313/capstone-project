package com.centria.cabbooking.controller;

import com.centria.cabbooking.entity.DriverProfile;
import com.centria.cabbooking.entity.User;
import com.centria.cabbooking.repository.DriverProfileRepository;
import com.centria.cabbooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for driver profile management operations
 * Handles HTTP requests for driver profile creation, query, update
 * Core functions: create profile, driver online/offline, update info
 */
@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverProfileRepository driverProfileRepository;
    private final UserRepository userRepository;

    /**
     * Create a new driver profile (bind to existing DRIVER role user)
     * POST /api/v1/drivers
     * @param driverProfile DriverProfile object with bound user ID and license number
     * @return Created DriverProfile object with HTTP 201 status
     * @throws EntityNotFoundException if bound user not found
     * @throws IllegalStateException if user role is not DRIVER or license number duplicate
     */
    @PostMapping
    public ResponseEntity<DriverProfile> createDriverProfile(@Valid @RequestBody DriverProfile driverProfile) {
        // Validate bound user exists and has correct role
        User user = userRepository.findById(driverProfile.getUser().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + driverProfile.getUser().getUserId()));

        if (!user.getRole().equals(User.Role.DRIVER)) {
            throw new IllegalStateException("User role must be DRIVER to create driver profile");
        }

        // Validate license number uniqueness
        if (driverProfileRepository.findByLicenseNumber(driverProfile.getLicenseNumber()) != null) {
            throw new IllegalStateException("License number already exists: " + driverProfile.getLicenseNumber());
        }

        DriverProfile createdProfile = driverProfileRepository.save(driverProfile);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    /**
     * Get driver profile by ID
     * GET /api/v1/drivers/{driverId}
     * @param driverId Unique ID of the driver profile
     * @return DriverProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @GetMapping("/{driverId}")
    public ResponseEntity<DriverProfile> getDriverProfileById(@PathVariable String driverId) {
        DriverProfile profile = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver profile not found with ID: " + driverId));
        return ResponseEntity.ok(profile);
    }

    /**
     * Get driver profile by associated user ID
     * GET /api/v1/drivers/user/{userId}
     * @param userId ID of the bound user
     * @return DriverProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverProfile> getDriverProfileByUserId(@PathVariable String userId) {
        DriverProfile profile = driverProfileRepository.findByUserUserId(userId);
        if (profile == null) {
            throw new EntityNotFoundException("Driver profile not found for user ID: " + userId);
        }
        return ResponseEntity.ok(profile);
    }

    /**
     * Get all driver profiles (filter by online status optional)
     * GET /api/v1/drivers
     * @param status Optional - driver status filter (ONLINE/OFFLINE/ON_TRIP)
     * @return List of DriverProfile objects with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<DriverProfile>> getAllDriverProfiles(
            @RequestParam(required = false) DriverProfile.DriverStatus status) {

        List<DriverProfile> profiles;
        if (status != null) {
            profiles = driverProfileRepository.findAll().stream()
                    .filter(d -> d.getCurrentStatus().equals(status))
                    .toList();
        } else {
            profiles = driverProfileRepository.findAll();
        }
        return ResponseEntity.ok(profiles);
    }

    /**
     * Update driver status (core online/offline function)
     * PATCH /api/v1/drivers/{driverId}/status
     * @param driverId Unique ID of the driver profile
     * @param status New status (ONLINE/OFFLINE/ON_TRIP)
     * @param currentLocation Optional - driver's current location (for dispatch)
     * @return Updated DriverProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @PatchMapping("/{driverId}/status")
    public ResponseEntity<DriverProfile> updateDriverStatus(
            @PathVariable String driverId,
            @RequestParam DriverProfile.DriverStatus status,
            @RequestParam(required = false) String currentLocation) {

        DriverProfile profile = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver profile not found with ID: " + driverId));

        profile.setCurrentStatus(status);
        // Update location if provided (for dispatch feature)
        if (currentLocation != null) {
            profile.setCurrentLocation(currentLocation);
        }

        DriverProfile updatedProfile = driverProfileRepository.save(profile);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Update driver profile information (license, rating)
     * PATCH /api/v1/drivers/{driverId}
     * @param driverId Unique ID of the driver profile
     * @param updateProfile DriverProfile object with fields to update
     * @return Updated DriverProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @PatchMapping("/{driverId}")
    public ResponseEntity<DriverProfile> updateDriverProfile(
            @PathVariable String driverId,
            @RequestBody DriverProfile updateProfile) {

        DriverProfile profile = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver profile not found with ID: " + driverId));

        // Safe update - only modify non-null fields (remove phoneNumber, entity has no this field)
        if (updateProfile.getLicenseNumber() != null) {
            // Re-validate license number uniqueness if updated
            if (!updateProfile.getLicenseNumber().equals(profile.getLicenseNumber())
                    && driverProfileRepository.findByLicenseNumber(updateProfile.getLicenseNumber()) != null) {
                throw new IllegalStateException("License number already exists: " + updateProfile.getLicenseNumber());
            }
            profile.setLicenseNumber(updateProfile.getLicenseNumber());
        }
        if (updateProfile.getRating() != null) {
            profile.setRating(updateProfile.getRating());
        }
        if (updateProfile.getCurrentLocation() != null) {
            profile.setCurrentLocation(updateProfile.getCurrentLocation());
        }

        DriverProfile updatedProfile = driverProfileRepository.save(profile);
        return ResponseEntity.ok(updatedProfile);
    }
}