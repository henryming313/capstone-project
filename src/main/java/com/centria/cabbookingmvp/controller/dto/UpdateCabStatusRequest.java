package com.centria.cabbookingmvp.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCabStatusRequest {

    @NotBlank
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}