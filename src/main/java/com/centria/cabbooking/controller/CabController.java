package com.centria.cabbooking.controller;

import com.centria.cabbooking.entity.Cab;
import com.centria.cabbooking.repository.CabRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for cab management operations
 * Handles HTTP requests for cab creation, query, update and filtering
 * Core APIs: POST(create), GET(query), PATCH(update)
 */
@RestController
@RequestMapping("/api/v1/cabs")
@RequiredArgsConstructor
public class CabController {

    private final CabRepository cabRepository;

    /**
     * Create a new cab
     * POST /api/v1/cabs
     * @param cab Cab object with required fields (carNumber, carModel, carType, perKmRate)
     * @return Created Cab object with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Cab> createCab(@Valid @RequestBody Cab cab) {
        Cab createdCab = cabRepository.save(cab);
        return new ResponseEntity<>(createdCab, HttpStatus.CREATED);
    }

    /**
     * Get cab by ID
     * GET /api/v1/cabs/{cabId}
     * @param cabId Unique ID of the cab
     * @return Cab object with HTTP 200 status
     * @throws EntityNotFoundException if cab not found
     */
    @GetMapping("/{cabId}")
    public ResponseEntity<Cab> getCabById(@PathVariable String cabId) {
        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found with ID: " + cabId));
        return ResponseEntity.ok(cab);
    }

    /**
     * Get all cabs (optional filtering by status and car type)
     * GET /api/v1/cabs
     * @param status Optional - cab status filter (AVAILABLE/BUSY/MAINTENANCE)
     * @param carType Optional - car type filter (ECONOMY/PREMIUM/XL)
     * @return List of Cab objects with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Cab>> getAllCabs(
            @RequestParam(required = false) Cab.CabStatus status,
            @RequestParam(required = false) Cab.CarType carType) {

        List<Cab> cabs;
        if (status != null && carType != null) {
            cabs = cabRepository.findByStatusAndCarType(status, carType);
        } else if (status != null) {
            cabs = cabRepository.findAll().stream()
                    .filter(c -> c.getStatus().equals(status))
                    .toList();
        } else if (carType != null) {
            cabs = cabRepository.findAll().stream()
                    .filter(c -> c.getCarType().equals(carType))
                    .toList();
        } else {
            cabs = cabRepository.findAll();
        }
        return ResponseEntity.ok(cabs);
    }

    /**
     * Update cab status (core update function)
     * PATCH /api/v1/cabs/{cabId}/status
     * @param cabId Unique ID of the cab
     * @param status New status for the cab (AVAILABLE/BUSY/MAINTENANCE)
     * @return Updated Cab object with HTTP 200 status
     * @throws EntityNotFoundException if cab not found
     */
    @PatchMapping("/{cabId}/status")
    public ResponseEntity<Cab> updateCabStatus(
            @PathVariable String cabId,
            @RequestParam Cab.CabStatus status) {

        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found with ID: " + cabId));
        cab.setStatus(status);
        Cab updatedCab = cabRepository.save(cab);
        return ResponseEntity.ok(updatedCab);
    }

    /**
     * Update cab basic information (carModel, perKmRate)
     * PATCH /api/v1/cabs/{cabId}
     * @param cabId Unique ID of the cab
     * @param updateCab Cab object with fields to update (only non-null fields are updated)
     * @return Updated Cab object with HTTP 200 status
     * @throws EntityNotFoundException if cab not found
     */
    @PatchMapping("/{cabId}")
    public ResponseEntity<Cab> updateCabInfo(
            @PathVariable String cabId,
            @RequestBody Cab updateCab) {

        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found with ID: " + cabId));

        // Update only non-null fields (safe update pattern)
        if (updateCab.getCarModel() != null) {
            cab.setCarModel(updateCab.getCarModel());
        }
        if (updateCab.getPerKmRate() != null) {
            cab.setPerKmRate(updateCab.getPerKmRate());
        }
        if (updateCab.getCarType() != null) {
            cab.setCarType(updateCab.getCarType());
        }

        Cab updatedCab = cabRepository.save(cab);
        return ResponseEntity.ok(updatedCab);
    }
}