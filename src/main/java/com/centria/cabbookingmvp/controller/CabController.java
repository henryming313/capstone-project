package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.controller.dto.CreateCabRequest;
import com.centria.cabbookingmvp.controller.dto.UpdateCabRequest;
import com.centria.cabbookingmvp.controller.dto.UpdateCabStatusRequest;
import com.centria.cabbookingmvp.entity.Cab;
import com.centria.cabbookingmvp.service.CabService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cabs")
public class CabController {

    private final CabService cabService;

    public CabController(CabService cabService) {
        this.cabService = cabService;
    }

    @PostMapping
    public ApiResponse<?> addCab(@Valid @RequestBody CreateCabRequest req) {
        Cab cab = cabService.addCab(req);

        return ApiResponse.success(Map.of(
                "id", cab.getId(),
                "plateNumber", cab.getPlateNumber(),
                "brand", cab.getBrand(),
                "model", cab.getModel(),
                "color", cab.getColor(),
                "cabType", cab.getCabType().name(),
                "driverId", cab.getDriverId(),
                "status", cab.getStatus().name()
        ));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateCab(@PathVariable Long id,
                                    @Valid @RequestBody UpdateCabRequest req) {
        Cab cab = cabService.updateCab(id, req);

        return ApiResponse.success(Map.of(
                "id", cab.getId(),
                "plateNumber", cab.getPlateNumber(),
                "brand", cab.getBrand(),
                "model", cab.getModel(),
                "color", cab.getColor(),
                "cabType", cab.getCabType().name(),
                "driverId", cab.getDriverId(),
                "status", cab.getStatus().name()
        ));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<?> updateStatus(@PathVariable Long id,
                                       @Valid @RequestBody UpdateCabStatusRequest req) {
        Cab cab = cabService.updateStatus(id, req.getStatus());

        return ApiResponse.success(Map.of(
                "id", cab.getId(),
                "plateNumber", cab.getPlateNumber(),
                "status", cab.getStatus().name()
        ));
    }

    @GetMapping("/driver/{driverId}")
    public ApiResponse<?> getByDriver(@PathVariable Long driverId) {
        List<Cab> cabs = cabService.getByDriverId(driverId);
        return ApiResponse.success(cabs);
    }

    @GetMapping("/status/{status}")
    public ApiResponse<?> getByStatus(@PathVariable String status) {
        List<Cab> cabs = cabService.getByStatus(status);
        return ApiResponse.success(cabs);
    }
}
