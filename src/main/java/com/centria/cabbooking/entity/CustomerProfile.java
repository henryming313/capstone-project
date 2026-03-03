package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_profiles")
@Data
public class CustomerProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String customerId;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String address;
	private Double rating = 0.0;
}