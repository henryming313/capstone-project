
```
docs/API.md
```
---

# CabBooking System API Documentation

# 1 System Overview

This project is a **Cab Booking MVP system** that simulates a ride-hailing platform.

The system allows passengers to request rides, drivers to accept ride requests, and administrators to manage the system.

System architecture:

```
Frontend (HTML / CSS / JavaScript)
        ↓
Axios HTTP Requests
        ↓
Spring Boot REST API
        ↓
MySQL Database
```

Backend Base URL

```
http://localhost:8081/api
```

---

# 2 Team Responsibilities

This section defines the responsibilities of each team member in the project.

## 2.1 Backend Developer

Responsible for backend development and API implementation.

Main responsibilities:

* Develop backend services using Spring Boot
* Design database entities:

```
User
Cab
TripBooking
```

* Implement REST APIs for system features
* Provide API documentation for frontend developers
* Ensure unified API response format
* Support frontend integration and debugging

Main API modules:

```
User module
Cab module
Trip module
```

---

## 2.2 Frontend Developer

Responsible for building the user interface and integrating frontend with backend APIs.

Technologies used:

```
HTML5
CSS3
JavaScript
```

API communication implemented using
Axios.

Frontend modules:

```
Passenger module
Driver module
Admin module
```

Example pages:

```
Login page
Registration page
Passenger dashboard
Driver dashboard
Trip page
Vehicle management page
```

Frontend tasks:

* Send API requests
* Display backend data
* Handle user interactions
* Update trip status dynamically

---

## 2.3 QA / UI Designer

Responsible for UI design, testing, and documentation support.

Main responsibilities:

* Design system UI layout and visual style
* Improve user experience and usability
* Create test cases
* Perform system testing

Testing includes:

```
Functional testing
API testing
Regression testing
```

Testing tools may include
Postman.

Other responsibilities:

* Report bugs
* Verify MVP functionality
* Maintain project repository on
  GitHub
* Assist with project documentation

---

# 3 System Architecture

System architecture overview:

```
Frontend
(HTML / CSS / JS)
       ↓
Axios HTTP Requests
       ↓
Spring Boot REST API
       ↓
Service Layer
       ↓
Repository Layer
       ↓
MySQL Database
```

Backend architecture:

```
Controller
Service
Repository
Entity
```

---

# 4 Response Format

All APIs follow a unified JSON response format.

## Success Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

## Error Response

```json
{
  "code": 1,
  "msg": "error message",
  "data": null
}
```

Field description:

| Field | Description            |
| ----- | ---------------------- |
| code  | 0 = success, 1 = error |
| msg   | response message       |
| data  | returned data          |

Frontend should always check the **code field**.

```
code = 0 → request successful
code = 1 → request failed
```

---

# 5 User Module APIs

## Register User

POST

```
/api/users/register
```

Request

```json
{
  "name": "Emma",
  "email": "emma@test.com",
  "phone": "0401234567",
  "password": "mypassword",
  "role": "RIDER"
}
```

Response

```json
{
  "code": 0,
  "msg": "user registered",
  "data": {
    "userId": 25
  }
}
```

---

## Login

POST

```
/api/users/login
```

Request

```json
{
  "identifier": "emma@test.com",
  "password": "mypassword"
}
```

Response

```json
{
  "code": 0,
  "msg": "login successful",
  "data": {
    "userId": 25,
    "role": "RIDER"
  }
}
```

---

# 6 Cab Module APIs

## Add Vehicle

POST

```
/api/cabs
```

Request

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

Response

```json
{
  "code": 0,
  "msg": "cab created",
  "data": {
    "cabId": 11
  }
}
```

---

# 7 Trip Module APIs

## Create Trip

POST

```
/api/trips
```

Request

```json
{
  "riderId": 12,
  "pickupLocation": "City Center",
  "dropoffLocation": "Airport"
}
```

Response

```json
{
  "code": 0,
  "msg": "trip created",
  "data": {
    "tripId": 60,
    "status": "REQUESTED"
  }
}
```

---

## Accept Trip

PUT

```
/api/trips/{id}/accept
```

Request

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
  "msg": "trip accepted",
  "data": {
    "tripId": 60,
    "status": "ACCEPTED"
  }
}
```

---

# 8 Integration Guide (Frontend)

Example Axios request:

```javascript
axios.post("http://localhost:8081/api/users/login", {
  identifier: "emma@test.com",
  password: "mypassword"
})
.then(res => {
  if(res.data.code === 0){
      console.log(res.data.data)
  } else {
      alert(res.data.msg)
  }
})
```

---

# 9 Testing Workflow

Recommended system testing order:

```
1 Register passenger account
2 Register driver account
3 Driver adds vehicle
4 Passenger creates trip
5 Driver accepts trip
6 Driver starts trip
7 Driver completes trip
8 Passenger checks trip history
```

---




