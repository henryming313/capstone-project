package com.centria.cabbookingmvp.service.impl;

import com.centria.cabbookingmvp.entity.*;
import com.centria.cabbookingmvp.entity.UserStatus;
import com.centria.cabbookingmvp.entity.TripStatus;
import com.centria.cabbookingmvp.repository.CabRepository;
import com.centria.cabbookingmvp.repository.TripBookingRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import com.centria.cabbookingmvp.service.AdminService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepo;
    private final CabRepository cabRepo;
    private final TripBookingRepository tripRepo;

    public AdminServiceImpl(UserRepository userRepo, CabRepository cabRepo, TripBookingRepository tripRepo) {
        this.userRepo = userRepo;
        this.cabRepo = cabRepo;
        this.tripRepo = tripRepo;
    }

    private User requireAdmin(Long adminId) {
        if (adminId == null) throw new IllegalArgumentException("adminId is required");
        User admin = userRepo.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found: " + adminId));
        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new IllegalArgumentException("Permission denied: not an admin");
        }
        if (admin.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Admin is not active");
        }
        return admin;
    }

    // ---------- Users ----------
    @Override
    public List<User> listUsers(Long adminId) {
        requireAdmin(adminId);
        return userRepo.findAll();
    }

    @Override
    public User updateUserStatus(Long adminId, Long userId, UserStatus status) {
        requireAdmin(adminId);
        if (status == null) throw new IllegalArgumentException("status is required");
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        u.setStatus(status);
        return userRepo.save(u);
    }

    // ---------- Cabs ----------
    @Override
    public List<Cab> listCabs(Long adminId) {
        requireAdmin(adminId);
        return cabRepo.findAll();
    }

    @Override
    public Cab setCabActive(Long adminId, Long cabId, boolean active) {
        requireAdmin(adminId);
        Cab cab = cabRepo.findById(cabId)
                .orElseThrow(() -> new IllegalArgumentException("Cab not found: " + cabId));
        cab.setActive(active);
        return cabRepo.save(cab);
    }

    // ---------- Trips ----------
    @Override
    public List<TripBooking> listTrips(Long adminId, TripStatus status) {
        requireAdmin(adminId);
        if (status == null) return tripRepo.findAll();
        return tripRepo.findByStatus(status);
    }

    @Override
    public TripBooking getTrip(Long adminId, Long tripId) {
        requireAdmin(adminId);
        return tripRepo.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));
    }

    // ---------- Admin dispatch ----------
    @Override
    public TripBooking assignTrip(Long adminId, Long tripId, Long driverId, Long cabId) {
        requireAdmin(adminId);

        TripBooking trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));

        if (trip.getStatus() != TripStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING trips can be assigned by admin");
        }

        User driver = userRepo.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
        if (!"DRIVER".equalsIgnoreCase(driver.getRole())) {
            throw new IllegalArgumentException("User is not a DRIVER");
        }

        Cab cab = cabRepo.findById(cabId)
                .orElseThrow(() -> new IllegalArgumentException("Cab not found: " + cabId));
        if (!cab.isActive()) {
            throw new IllegalArgumentException("Cab is inactive");
        }
        // 你当前系统的“车归属”规则：cab.driverId 必须等于 driverId
        if (cab.getDriverId() == null || !cab.getDriverId().equals(driverId)) {
            throw new IllegalArgumentException("Cab does not belong to this driver");
        }

        trip.setDriver(driver);
        trip.setCab(cab);
        trip.setStatus(TripStatus.ACCEPTED);

        return tripRepo.save(trip);
    }
}
