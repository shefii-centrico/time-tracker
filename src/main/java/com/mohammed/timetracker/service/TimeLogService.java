package com.mohammed.timetracker.service;

import com.mohammed.timetracker.model.Role;
import com.mohammed.timetracker.model.Task;
import com.mohammed.timetracker.model.TimeLog;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.repository.TaskRepository;
import com.mohammed.timetracker.repository.TimeLogRepository;
import com.mohammed.timetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TimeLogService(TimeLogRepository timeLogRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.timeLogRepository = timeLogRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TimeLog logTime(Long taskId, TimeLog timeLog, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Employees can only log time for tasks assigned to them
        if (user.getRole() == Role.EMPLOYEE) {
            if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
                throw new RuntimeException("You are not assigned to this task");
            }
        }

        timeLog.setTask(task);
        timeLog.setUser(user);
        return timeLogRepository.save(timeLog);
    }

    public List<TimeLog> getFiltered(Long taskId, Long userId, LocalDate startDate, LocalDate endDate) {
        return timeLogRepository.findFiltered(taskId, userId, startDate, endDate);
    }

    public TimeLog updateTimeLog(Long id, Double hours, LocalDate date) {
        TimeLog log = timeLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeLog not found with id: " + id));
        log.setHours(hours);
        log.setDate(date);
        return timeLogRepository.save(log);
    }

    public List<TimeLog> getMyTimeLogs(String username) {
        return timeLogRepository.findByUser_Username(username);
    }

    public void deleteTimeLog(Long id) {
        if (!timeLogRepository.existsById(id)) {
            throw new RuntimeException("TimeLog not found with id: " + id);
        }
        timeLogRepository.deleteById(id);
    }
}

