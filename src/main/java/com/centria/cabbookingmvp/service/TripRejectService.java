package com.centria.cabbookingmvp.service;

import com.centria.cabbookingmvp.entity.TripBooking;

import java.util.List;

public interface TripRejectService {

    void rejectTrip(Long tripId, Long driverId);

    List<TripBooking> listPendingTripsForDriver(Long driverId);
}
