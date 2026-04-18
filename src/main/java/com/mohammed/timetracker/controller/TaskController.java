package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.model.Task;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.service.TaskService;
import com.mohammed.timetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // TEAM_LEAD and ADMIN can fetch the employees list for the assign dropdown
    @GetMapping("/assignable-users")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<List<User>> getAssignableUsers() {
        return ResponseEntity.ok(userService.getEmployees());
    }

    // TL, Admin, PM can create tasks
    @PostMapping
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task, Authentication auth) {
        return ResponseEntity.ok(taskService.createTask(task, auth.getName()));
    }

    // TL, Admin, PM see all tasks
    @GetMapping
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Get single task by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // Employee sees only their assigned tasks
    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Task>> getMyTasks(Authentication auth) {
        return ResponseEntity.ok(taskService.getMyTasks(auth.getName()));
    }

    // TL, Admin, PM can edit tasks
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    // TL and Admin can assign a task to an employee
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<Task> assignTask(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(taskService.assignTask(id, userId));
    }

    // Employee can update their own task status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id,
                                             @RequestParam String status,
                                             Authentication auth) {
        return ResponseEntity.ok(taskService.updateStatus(id, status, auth.getName()));
    }

    // TL and Admin can delete tasks
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // Team summary: per-employee workload stats for Team Lead dashboard
    @GetMapping("/team-summary")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getTeamSummary() {
        List<User> employees = userService.getEmployees();
        List<Task> allTasks = taskService.getAllTasks();
        LocalDate today = LocalDate.now();

        List<Map<String, Object>> summary = employees.stream().map(emp -> {
            List<Task> empTasks = allTasks.stream()
                    .filter(t -> t.getAssignedTo() != null && t.getAssignedTo().getId().equals(emp.getId()))
                    .collect(Collectors.toList());

            long todo = empTasks.stream().filter(t -> t.getStatus().name().equals("TODO")).count();
            long inProgress = empTasks.stream().filter(t -> t.getStatus().name().equals("IN_PROGRESS")).count();
            long done = empTasks.stream().filter(t -> t.getStatus().name().equals("DONE")).count();
            long overdue = empTasks.stream().filter(t ->
                    t.getDueDate() != null && t.getDueDate().isBefore(today) && !t.getStatus().name().equals("DONE")
            ).count();
            long dueSoon = empTasks.stream().filter(t ->
                    t.getDueDate() != null && !t.getDueDate().isBefore(today) &&
                    t.getDueDate().isBefore(today.plusDays(4)) && !t.getStatus().name().equals("DONE")
            ).count();

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", emp.getId());
            row.put("fullName", emp.getFullName());
            row.put("username", emp.getUsername());
            row.put("email", emp.getEmail());
            row.put("totalTasks", empTasks.size());
            row.put("todo", todo);
            row.put("inProgress", inProgress);
            row.put("done", done);
            row.put("overdue", overdue);
            row.put("dueSoon", dueSoon);
            row.put("activeTasks", empTasks.stream()
                    .filter(t -> !t.getStatus().name().equals("DONE"))
                    .map(t -> Map.of(
                            "id", t.getId(),
                            "title", t.getTitle(),
                            "status", t.getStatus().name(),
                            "priority", t.getPriority() != null ? t.getPriority() : "—",
                            "dueDate", t.getDueDate() != null ? t.getDueDate().toString() : ""
                    ))
                    .collect(Collectors.toList()));
            return row;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(summary);
    }
}

