package com.example.t24mock.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.t24mock.dto.UserRequest;
import com.example.t24mock.dto.UserResponse;
import com.example.t24mock.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserRequest request) {
        if (request.getUserId() == null || request.getUserId().isBlank()) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Missing required field: userId");
            return ResponseEntity.badRequest().body(body);
        }

        if (request.getUserName() == null || request.getUserName().isBlank()) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Missing required field: userName");
            return ResponseEntity.badRequest().body(body);
        }

        try {
            userService.createUser(request);
            Map<String, Object> body = new HashMap<>();
            body.put("status", "SUCCESS");
            body.put("userId", request.getUserId());
            body.put("message", "User created successfully");
            body.put("timestamp", Instant.now().toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        Optional<UserResponse> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            Map<String, String> body = new HashMap<>();
            body.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequest request) {
        if (request.getUserName() == null || request.getUserName().isBlank()) {
            Map<String, String> body = new HashMap<>();
            body.put("error", "Missing required field: userName");
            return ResponseEntity.badRequest().body(body);
        }

        Optional<UserResponse> updatedUser = userService.updateUser(userId, request);
        if (updatedUser.isEmpty()) {
            Map<String, String> body = new HashMap<>();
            body.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        return ResponseEntity.ok(updatedUser.get());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        if (!userService.deleteUser(userId)) {
            Map<String, String> body = new HashMap<>();
            body.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        Map<String, String> body = new HashMap<>();
        body.put("status", "SUCCESS");
        body.put("message", "User deleted successfully");
        return ResponseEntity.ok(body);
    }
}
