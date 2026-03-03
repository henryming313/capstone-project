package com.centria.cabbooking.controller;

import com.centria.cabbooking.entity.CustomerProfile;
import com.centria.cabbooking.entity.User;
import com.centria.cabbooking.repository.CustomerProfileRepository;
import com.centria.cabbooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for customer profile management operations
 * Handles HTTP requests for customer profile creation, query, update
 * Core APIs: POST(create), GET(query), PATCH(update)
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerProfileRepository customerProfileRepository;
    private final UserRepository userRepository;

    /**
     * Create a new customer profile (bind to existing CUSTOMER role user)
     * POST /api/v1/customers
     * @param customerProfile CustomerProfile object with bound user ID
     * @return Created CustomerProfile object with HTTP 201 status
     * @throws EntityNotFoundException if bound user not found
     * @throws IllegalStateException if user role is not CUSTOMER
     */
    @PostMapping
    public ResponseEntity<CustomerProfile> createCustomerProfile(@Valid @RequestBody CustomerProfile customerProfile) {
        // Validate bound user exists and has correct role
        User user = userRepository.findById(customerProfile.getUser().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + customerProfile.getUser().getUserId()));

        if (!user.getRole().equals(User.Role.CUSTOMER)) {
            throw new IllegalStateException("User role must be CUSTOMER to create customer profile");
        }

        CustomerProfile createdProfile = customerProfileRepository.save(customerProfile);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    /**
     * Get customer profile by ID
     * GET /api/v1/customers/{customerId}
     * @param customerId Unique ID of the customer profile
     * @return CustomerProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerProfile> getCustomerProfileById(@PathVariable String customerId) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer profile not found with ID: " + customerId));
        return ResponseEntity.ok(profile);
    }

    /**
     * Get customer profile by associated user ID
     * GET /api/v1/customers/user/{userId}
     * @param userId ID of the bound user
     * @return CustomerProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerProfile> getCustomerProfileByUserId(@PathVariable String userId) {
        CustomerProfile profile = customerProfileRepository.findByUserUserId(userId);
        if (profile == null) {
            throw new EntityNotFoundException("Customer profile not found for user ID: " + userId);
        }
        return ResponseEntity.ok(profile);
    }

    /**
     * Get all customer profiles (simple list without filtering)
     * GET /api/v1/customers
     * @return List of all CustomerProfile objects with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<CustomerProfile>> getAllCustomerProfiles() {
        List<CustomerProfile> profiles = customerProfileRepository.findAll();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Update customer profile information (address, rating)
     * PATCH /api/v1/customers/{customerId}
     * @param customerId Unique ID of the customer profile
     * @param updateProfile CustomerProfile object with fields to update
     * @return Updated CustomerProfile object with HTTP 200 status
     * @throws EntityNotFoundException if profile not found
     */
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerProfile> updateCustomerProfile(
            @PathVariable String customerId,
            @RequestBody CustomerProfile updateProfile) {

        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer profile not found with ID: " + customerId));

        // Safe update - only modify non-null fields (remove phoneNumber, entity has no this field)
        if (updateProfile.getAddress() != null) {
            profile.setAddress(updateProfile.getAddress());
        }
        if (updateProfile.getRating() != null) {
            profile.setRating(updateProfile.getRating());
        }

        CustomerProfile updatedProfile = customerProfileRepository.save(profile);
        return ResponseEntity.ok(updatedProfile);
    }
}