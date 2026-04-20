package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.entity.*;
import com.centria.cabbookingmvp.entity.UserStatus;
import com.centria.cabbookingmvp.entity.TripStatus;

import java.util.List;

public interface AdminService {

    // Users
    List<User> listUsers(Long adminId);
    User updateUserStatus(Long adminId, Long userId, UserStatus status);

    // Cabs
    List<Cab> listCabs(Long adminId);
    Cab setCabActive(Long adminId, Long cabId, boolean active);

    // Trips
    List<TripBooking> listTrips(Long adminId, TripStatus status); // status 可传 null
    TripBooking getTrip(Long adminId, Long tripId);

    // Assign driver + cab to a trip (Admin dispatch)
    TripBooking assignTrip(Long adminId, Long tripId, Long driverId, Long cabId);
}