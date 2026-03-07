
```text
docs/API.md
```

---

# CabBooking MVP API Documentation

Base URL

```
http://localhost:8081
```

---

# 1 User Registration

Create a new user account.

### Endpoint

```
POST /api/register
```

### Request Body

```json
{
  "name": "Bob",
  "email": "bob@test.com",
  "phone": "0400000003",
  "password": "123456"
}
```

### Parameters

| Field    | Type   | Required | Description   |
| -------- | ------ | -------- | ------------- |
| name     | String | Yes      | User name     |
| email    | String | Yes      | Email address |
| phone    | String | Yes      | Phone number  |
| password | String | Yes      | User password |

---

### Success Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 3,
    "name": "Bob",
    "email": "bob@test.com",
    "phone": "0400000003",
    "status": "ACTIVE"
  }
}
```

---

### Error Response

Example when email already exists:

```json
{
  "code": 1,
  "msg": "Email already exists",
  "data": null
}
```

---

# 2 User Login

Login using email or phone.

### Endpoint

```
POST /api/login
```

### Request Body

```json
{
  "identifier": "bob@test.com",
  "password": "123456"
}
```

### Success Response

```json
{
  "code": 0,
  "msg": "login success",
  "data": {
    "id": 3,
    "name": "Bob",
    "email": "bob@test.com",
    "phone": "0400000003"
  }
}
```

---

### Error Response

```json
{
  "code": 1,
  "msg": "Invalid password",
  "data": null
}
```

---

# 3 Get User Info

Get user information by ID.

### Endpoint

```
GET /api/users/{id}
```

Example

```
GET /api/users/3
```

---

### Success Response

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 3,
    "name": "Bob",
    "email": "bob@test.com",
    "phone": "0400000003",
    "status": "ACTIVE"
  }
}
```

---

# Response Format

All API responses follow this structure:

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

| Field | Description   |
| ----- | ------------- |
| code  | 0 = success   |
| msg   | message       |
| data  | response data |

---
Hi, I have finished the basic backend API for the CabBooking MVP.

I wrote a simple API documentation (API.md). It includes three endpoints:

1. POST /api/register – user registration
2. POST /api/login – user login
3. GET /api/users/{id} – get user information

You can follow the request examples and response formats in the document to connect the frontend.

For now the backend runs locally at:

http://localhost:8081

So when you develop the UI, just call these APIs.

What I need from you

1. The UI pages (Register page / Login page)
2. The API request format you will send
3. Any additional fields you may need from the backend

If you need new APIs (for example booking a ride or showing driver info), tell me and I will implement them.
