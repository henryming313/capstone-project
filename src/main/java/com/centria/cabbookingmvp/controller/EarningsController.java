package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.controller.dto.DriverEarningsResponse;
import com.centria.cabbookingmvp.service.EarningsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/earnings")
public class EarningsController {

    private final EarningsService earningsService;

    public EarningsController(EarningsService earningsService) {
        this.earningsService = earningsService;
    }

    @GetMapping("/driver/{driverId}")
    public ApiResponse<DriverEarningsResponse> getDriverEarnings(@PathVariable Long driverId) {
        return ApiResponse.success(earningsService.getDriverEarnings(driverId));
    }
}
