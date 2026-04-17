package com.mohammed.timetracker.service;

import com.mohammed.timetracker.model.Task;
import com.mohammed.timetracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, Task updated) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updated.getTitle());
            task.setDescription(updated.getDescription());
            task.setStatus(updated.getStatus());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}
