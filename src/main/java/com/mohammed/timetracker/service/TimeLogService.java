package com.mohammed.timetracker.service;

import com.mohammed.timetracker.model.TimeLog;
import com.mohammed.timetracker.repository.TimeLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;

    public TimeLogService(TimeLogRepository timeLogRepository) {
        this.timeLogRepository = timeLogRepository;
    }

    public TimeLog logTime(TimeLog timeLog) {
        return timeLogRepository.save(timeLog);
    }

    public List<TimeLog> getByTaskId(Long taskId) {
        if (taskId != null) {
            return timeLogRepository.findByTaskId(taskId);
        }
        return timeLogRepository.findAll();
    }
}
