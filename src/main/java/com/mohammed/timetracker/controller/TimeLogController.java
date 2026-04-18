package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.model.TimeLog;
import com.mohammed.timetracker.service.TimeLogService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    // TL and Admin view all time logs with optional filters
    @GetMapping
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<List<TimeLog>> getTimeLogs(
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(timeLogService.getFiltered(taskId, userId, startDate, endDate));
    }

    // TL and Admin can edit time logs
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<TimeLog> updateTimeLog(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Double hours = Double.valueOf(body.get("hours").toString());
        LocalDate date = LocalDate.parse(body.get("date").toString());
        return ResponseEntity.ok(timeLogService.updateTimeLog(id, hours, date));
    }

    // TL and Admin can delete time logs
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTimeLog(@PathVariable Long id) {
        timeLogService.deleteTimeLog(id);
        return ResponseEntity.noContent().build();
    }
}

