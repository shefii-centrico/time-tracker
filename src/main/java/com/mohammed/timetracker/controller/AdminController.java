package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.dto.CreateUserRequest;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Any authenticated user can view their own profile
    @GetMapping("/users/me")
    public ResponseEntity<User> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(userService.findByUsername(auth.getName()));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // TEAM_LEAD needs the employees list to assign tasks
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEAM_LEAD')")
    public ResponseEntity<List<User>> getEmployees() {
        return ResponseEntity.ok(userService.getEmployees());
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateRole(@PathVariable Long id, @RequestParam String role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

