package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import com.centria.cabbookingmvp.controller.dto.LoginRequest;
import com.centria.cabbookingmvp.controller.dto.RegisterRequest;
import com.centria.cabbookingmvp.entity.User;
import com.centria.cabbookingmvp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/register
    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest req) {
        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());


        u.setPasswordHash(req.getPassword());

        User saved = userService.register(u);


        return ApiResponse.success(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "email", saved.getEmail(),
                "phone", saved.getPhone(),
                "status", saved.getStatus().name()
        ));
    }

    // POST /api/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        User user = userService.login(req.getIdentifier(), req.getPassword());

        return ResponseEntity.ok(Map.of(
                "message", "login success",
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone()
        ));
    }

    // GET /api/users/{id}
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "status", user.getStatus().name()
        ));
    }
}
