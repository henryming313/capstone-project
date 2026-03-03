package com.centria.cabbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String userId;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(unique = true, nullable = false)
	private String mobileNumber;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;

	public enum Role {
		ADMIN, DRIVER, CUSTOMER
	}

	public enum UserStatus {
		ACTIVE, INACTIVE, SUSPENDED
	}
}