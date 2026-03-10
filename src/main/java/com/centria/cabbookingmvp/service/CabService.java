package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.controller.dto.CreateCabRequest;
import com.centria.cabbookingmvp.controller.dto.UpdateCabRequest;
import com.centria.cabbookingmvp.entity.Cab;
import com.centria.cabbookingmvp.entity.CabStatus;
import com.centria.cabbookingmvp.repository.CabRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabService {

    private final CabRepository cabRepo;
    private final UserRepository userRepo;

    public CabService(CabRepository cabRepo, UserRepository userRepo) {
        this.cabRepo = cabRepo;
        this.userRepo = userRepo;
    }

    public Cab addCab(CreateCabRequest req) {
        if (req.getDriverId() == null) {
            throw new RuntimeException("Driver cannot be null");
        }

        if (userRepo.findById(req.getDriverId()).isEmpty()) {
            throw new RuntimeException("Driver not found");
        }

        if (cabRepo.findByPlateNumber(req.getPlateNumber()).isPresent()) {
            throw new RuntimeException("Plate number already exists");
        }

        Cab cab = new Cab();
        cab.setPlateNumber(req.getPlateNumber());
        cab.setBrand(req.getBrand());
        cab.setModel(req.getModel());
        cab.setColor(req.getColor());
        cab.setCabType(req.getCabType());
        cab.setDriverId(req.getDriverId());
        cab.setStatus(CabStatus.OFFLINE);
        cab.setActive(true);

        return cabRepo.save(cab);
    }

    public Cab updateCab(Long id, UpdateCabRequest req) {
        Cab cab = cabRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cab not found: " + id));

        if (req.getBrand() != null) cab.setBrand(req.getBrand());
        if (req.getModel() != null) cab.setModel(req.getModel());
        if (req.getColor() != null) cab.setColor(req.getColor());
        if (req.getCabType() != null) cab.setCabType(req.getCabType());

        return cabRepo.save(cab);
    }

    public Cab updateStatus(Long id, String statusText) {
        Cab cab = cabRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cab not found: " + id));

        CabStatus status;
        try {
            status = CabStatus.valueOf(statusText.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status must be ONLINE or OFFLINE");
        }

        cab.setStatus(status);
        return cabRepo.save(cab);
    }

    public List<Cab> getByDriverId(Long driverId) {
        return cabRepo.findByDriverId(driverId);
    }

    public List<Cab> getByStatus(String statusText) {
        CabStatus status;
        try {
            status = CabStatus.valueOf(statusText.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status must be ONLINE or OFFLINE");
        }

        return cabRepo.findByStatus(status);
    }
}
