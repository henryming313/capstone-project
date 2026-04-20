package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.controller.dto.FareEstimateRequest;
import com.centria.cabbookingmvp.controller.dto.FareEstimateResponse;
import com.centria.cabbookingmvp.service.FareEstimateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fare")
public class FareController {

    private final FareEstimateService fareEstimateService;

    public FareController(FareEstimateService fareEstimateService) {
        this.fareEstimateService = fareEstimateService;
    }

    @PostMapping("/estimate")
    public ApiResponse<FareEstimateResponse> estimate(@Valid @RequestBody FareEstimateRequest req) {
        return ApiResponse.success(
                fareEstimateService.estimate(req.getPickupLocation(), req.getDropoffLocation())
        );
    }
}
