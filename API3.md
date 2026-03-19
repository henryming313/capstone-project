

---

# 🚕 CabBooking System API Documentation

---

# 1 System Overview

This project is a **Cab Booking MVP system**.

System flow:

```text
Passenger → Create Trip → Driver Accept → Start → Complete
```

Architecture:

```text
Frontend (HTML / CSS / JS)
        ↓
Axios HTTP Requests
        ↓
Spring Boot REST API
        ↓
MySQL Database
```

Base URL:

```text
http://localhost:8081/api
```

---

# 2 Team Responsibilities

## Backend Developer

* Develop API using Spring Boot
* Design database (User / Cab / TripBooking)
* Provide API documentation
* Maintain business logic

## Frontend Developer

* HTML + CSS + JavaScript
* Use Axios to call APIs
* Build Passenger / Driver UI

## QA / UI

* UI design
* Functional testing
* API testing
* Bug reporting
* Maintain GitHub

---

# 3 Response Format

## Success

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

## Error

```json
{
  "code": 1,
  "msg": "error message",
  "data": null
}
```

---

# 4 User Module

## Register

POST `/api/users/register`

```json
{
  "name": "Alice",
  "email": "alice@test.com",
  "phone": "0400000002",
  "password": "123456",
  "role": "RIDER"
}
```

---

## Login

POST `/api/users/login`

```json
{
  "identifier": "alice@test.com",
  "password": "123456"
}
```

---

# 5 Cab Module

## Add Cab

POST `/api/cabs`

```json
{
  "driverId": 8,
  "brand": "Toyota",
  "model": "Corolla",
  "color": "White",
  "plateNumber": "FIN-775",
  "cabType": "SEDAN"
}
```

---

# 6 Trip Module

## 6.1 Create Trip

POST `/api/trips`

```json
{
  "riderId": 2,
  "pickupLocation": "Kokkola Railway Station",
  "dropoffLocation": "Centria University"
}
```

Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 16,
    "pickupLocation": "Kokkola Railway Station",
    "dropoffLocation": "Centria University",
    "status": "PENDING"
  }
}
```

---

## 6.2 Accept Trip

PUT `/api/trips/{id}/accept`

```json
{
  "driverId": 8,
  "cabId": 11
}
```

Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 16,
    "status": "ACCEPTED"
  }
}
```

---

## 6.3 Start Trip

PUT `/api/trips/{id}/start`

Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 16,
    "status": "ONGOING"
  }
}
```

---

## 6.4 Complete Trip

PUT `/api/trips/{id}/complete`

Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 16,
    "status": "COMPLETED"
  }
}
```

---

## 6.5 Cancel Trip

PUT `/api/trips/{id}/cancel`

Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 16,
    "status": "CANCELLED"
  }
}
```

---

## 6.6 Get Passenger Trips

GET `/api/trips/passenger/{userId}`

---

## 6.7 Get Driver Trips

GET `/api/trips/driver/{driverId}`

---

## 6.8 Get Trips By Status

GET `/api/trips/status/{status}`

Example:

```text
/api/trips/status/PENDING
```

---

# 7 Kokkola Fixed Locations (IMPORTANT)

Frontend must only use these locations:

```text
Centria University
Kokkola Railway Station
Kokkola Bus Station
City Center
Chydenia Shopping Center
```

---

# 8 Frontend Integration Example

```javascript
axios.post("http://localhost:8081/api/trips", {
  riderId: 2,
  pickupLocation: "Kokkola Railway Station",
  dropoffLocation: "Centria University"
})
.then(res => {
  if(res.data.code === 0){
      console.log("success")
  } else {
      alert(res.data.msg)
  }
})
```

---

# 9 Testing Workflow

```text
1 Register passenger
2 Register driver
3 Driver adds cab
4 Passenger creates trip
5 Driver accepts trip
6 Driver starts trip
7 Driver completes trip
8 Passenger checks history
```

---
