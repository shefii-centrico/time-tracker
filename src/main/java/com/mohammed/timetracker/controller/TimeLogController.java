package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.model.TimeLog;
import com.mohammed.timetracker.service.TimeLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-logs")
public class TimeLogController {

    private final TimeLogService timeLogService;

    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    // Employees log time (only for their assigned tasks)
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<TimeLog> logTime(@RequestParam Long taskId,
                                           @Valid @RequestBody TimeLog timeLog,
                                           Authentication auth) {
        return ResponseEntity.ok(timeLogService.logTime(taskId, timeLog, auth.getName()));
    }

    // Employee views their own time logs
    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<TimeLog>> getMyTimeLogs(Authentication auth) {
        return ResponseEntity.ok(timeLogService.getMyTimeLogs(auth.getName()));
    }

    // TL and Admin view all time logs (optionally filtered by task)
    @GetMapping
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<List<TimeLog>> getTimeLogs(@RequestParam(required = false) Long taskId) {
        return ResponseEntity.ok(timeLogService.getByTaskId(taskId));
    }

    // TL and Admin can delete time logs
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTimeLog(@PathVariable Long id) {
        timeLogService.deleteTimeLog(id);
        return ResponseEntity.noContent().build();
    }
}

