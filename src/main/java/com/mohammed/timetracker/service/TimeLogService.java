package com.mohammed.timetracker.service;

import com.mohammed.timetracker.model.Task;
import com.mohammed.timetracker.model.TimeLog;
import com.mohammed.timetracker.repository.TaskRepository;
import com.mohammed.timetracker.repository.TimeLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final TaskRepository taskRepository;

    public TimeLogService(TimeLogRepository timeLogRepository, TaskRepository taskRepository) {
        this.timeLogRepository = timeLogRepository;
        this.taskRepository = taskRepository;
    }

    public TimeLog logTime(Long taskId, TimeLog timeLog) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        timeLog.setTask(task);
        return timeLogRepository.save(timeLog);
    }

    public List<TimeLog> getByTaskId(Long taskId) {
        if (taskId != null) {
            return timeLogRepository.findByTask_Id(taskId);
        }
        return timeLogRepository.findAll();
    }

    public void deleteTimeLog(Long id) {
        if (!timeLogRepository.existsById(id)) {
            throw new RuntimeException("TimeLog not found with id: " + id);
        }
        timeLogRepository.deleteById(id);
    }
}
