package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.model.TimeLog;
import com.mohammed.timetracker.service.TimeLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-logs")
@CrossOrigin(origins = "http://localhost:3000")
public class TimeLogController {

    private final TimeLogService timeLogService;

    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @PostMapping
    public ResponseEntity<TimeLog> logTime(@RequestBody TimeLog timeLog) {
        return ResponseEntity.ok(timeLogService.logTime(timeLog));
    }

    @GetMapping
    public ResponseEntity<List<TimeLog>> getTimeLogs(@RequestParam(required = false) Long taskId) {
        return ResponseEntity.ok(timeLogService.getByTaskId(taskId));
    }
}
