package com.centria.cabbookingmvp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// Spring Boot will create database tables based on this class.
@Entity
// Specify the database table name as users
@Table(
        name = "users",
       // The database will automatically guarantee uniqueness.
        uniqueConstraints = {
                // Email addresses cannot be duplicated
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                // Phone numbers cannot be duplicated
                @UniqueConstraint(name = "uk_users_phone", columnNames = "phone")
        }
)
public class User {

    @Id // id is the database primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id is the database primary key
    private Long id;

    // Username
    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80) // Cannot be empty // Maximum length 80
    private String name;

    // User email
    @Email
    @NotBlank
    @Size(max = 190)
    @Column(nullable = false, length = 190)
    private String email;

    // User's mobile number
    @NotBlank
    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String phone;

    // Store the hash value of the password
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String passwordHash;

    // User Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private UserStatus status = UserStatus.ACTIVE;

    // User Roles
    // The default is RIDER (passenger)
    // The driver can be set as DRIVER
    @Column(nullable = false, length = 16)
    private String role = "RIDER"; // MVP 先用 String

    // JPA requires an empty constructor function
    public User() {}

    // Used for reading and modifying data
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}