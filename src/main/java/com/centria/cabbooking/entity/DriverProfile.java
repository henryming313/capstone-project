package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "driver_profiles")
@Data
public class DriverProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String driverProfileId;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(unique = true, nullable = false)
	private String licenseNumber;

	private String currentLocation;

	private Double rating = 0.0;

	@Enumerated(EnumType.STRING)
	private DriverStatus currentStatus = DriverStatus.OFFLINE;

	public enum DriverStatus {
		ONLINE, OFFLINE, ON_TRIP
	}
}