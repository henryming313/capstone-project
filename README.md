

---

# 🚖 Cab Booking System

### Capstone Project – Centria University of Applied Sciences

---

## 📌 Project Overview

Cab Booking System is an Uber-like ride-hailing MVP developed using **Spring Boot** and MySQL.

The system provides a structured, role-based transportation platform tailored for Central Ostrobothnia, Finland.

It supports three user roles:

* 👤 Passenger (Rider)
* 🚗 Driver
* 🛠️ Administrator

The system demonstrates:

* RESTful API development
* Layered MVC architecture
* Role-based access control
* Trip lifecycle management
* Earnings calculation
* Rating system
* Admin management

---

## 🎯 Problem Statement

In Central Ostrobothnia:

* Public transportation options are limited
* Taxi dispatching is often manual
* Pricing lacks transparency
* No unified local digital booking system exists

This project provides a digital cab booking platform to address these issues.

---

## 👥 Team Members

| Name           | Role                 |
| -------------- | -------------------- |
| Mazharul Islam | Project Manager      |
| Zheng Minghao  | Backend Developer    |
| Huang Yuyuan   | Full-Stack Developer |
| Peng Miaoyang  | QA & UI Developer    |

---

## 🛠️ Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven

### Frontend

* HTML5
* CSS3
* JavaScript
* Axios

### Database

* MySQL 8.0
* ## Database Setup

1. Install MySQL 8+
2. Create database:

CREATE DATABASE cabbooking_mvp;

3. Import the provided SQL file:

mysql -u root -p cabbooking_mvp < db/cabbooking_mvp.sql

4. Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/cabbooking_mvp
spring.datasource.username=your_username
spring.datasource.password=your_password

5. Run the project:

./mvnw spring-boot:run

---

## 🏗️ System Architecture

The system follows a layered architecture:

```
Frontend (HTML / JS)
        ↓
Controller Layer (REST API)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (JPA)
        ↓
MySQL Database
```

### Architectural Highlights

* RESTful API design
* MVC layered structure
* Role-based validation
* Trip status lifecycle control
* Backend-level security checks
* Cab ownership validation

---

## 🚀 Core Features

### 👤 Passenger

* Register / Login
* Book a ride
* Estimate fare
* Cancel trip (if allowed)
* View trip history
* Rate completed trips

### 🚗 Driver

* Accept / Reject rides
* Start / Complete trips
* View completed trips
* View earnings statistics

### 🛠️ Admin

* Manage users (activate / ban)
* Manage vehicles
* Assign drivers to trips
* Monitor system data

---

## 🔄 Trip Lifecycle

The system enforces strict status transitions:

```
PENDING
   ↓
ACCEPTED
   ↓
IN_PROGRESS
   ↓
COMPLETED
```

Or:

```
PENDING / ACCEPTED → CANCELLED
```

Invalid transitions are blocked at backend level.

---

## 💰 Earnings Calculation

* Earnings are calculated only for `COMPLETED` trips.
* Fare is estimated and persisted when a trip is completed.
* Driver earnings include:

  * Total completed trips
  * Total earnings
  * Average fare

---

## 🔐 Security & Validation

* Role validation (RIDER / DRIVER / ADMIN)
* Account status validation (ACTIVE / BANNED)
* Driver–Cab ownership validation
* Duplicate registration prevention
* Backend-level permission enforcement

---

## 🗄 Database Design

Main tables:

* `users`
* `cabs`
* `trip_bookings`
* `ratings`
* `driver_cab_assignment`
* `trip_rejections`

Relationships:

* One Rider → Many Trips
* One Driver → Many Trips
* One Driver → One or More Cabs
* One Trip → One Rating

---

## ⚙️ How to Run

### 1️⃣ Clone Repository

```bash
git clone <repository-url>
```

### 2️⃣ Create MySQL Database

```sql
CREATE DATABASE cabbooking_mvp;
```

### 3️⃣ Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cabbooking_mvp
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
server.port=8081
```

### 4️⃣ Run Application

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🌐 Access the System

After the server starts, open:

```
http://localhost:8081/frontend/login.html
```

This page is the entry point for:

* Passenger
* Driver
* Admin

---

## 📊 Market Positioning

Compared to Uber:

* No high commission fees
* Designed for small regional companies
* Supports local branding
* Lightweight deployment

Target users:

* Local taxi companies
* Small transport businesses
* Residents and tourists in Kokkola

---

## 📅 Project Timeline

| Period    | Phase                                  |
| --------- | -------------------------------------- |
| Feb – Mar | System design & backend implementation |
| April     | Frontend integration & testing         |
| May       | Final optimization & delivery          |

---

## 📌 Future Improvements

* Google Maps API integration
* Real-time GPS tracking
* Online payment integration
* JWT-based authentication
* Mobile application support

---

## 📜 License

Developed for academic purposes
Centria UAS Capstone Project

---



