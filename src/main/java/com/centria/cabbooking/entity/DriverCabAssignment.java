package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents the assignment relationship between driver and cab
 * Tracks current and historical assignments
 */
@Entity
@Table(name = "driver_cab_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverCabAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "assignment_id")
	private String assignmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id", nullable = false)
	private DriverProfile driver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cab_id", nullable = false)
	private Cab cab;

	@CreationTimestamp
	@Column(name = "assigned_at", nullable = false)
	private LocalDateTime assignedAt;

	@Column(name = "released_at")
	private LocalDateTime releasedAt;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;
}