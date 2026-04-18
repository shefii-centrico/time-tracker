package com.mohammed.timetracker.service;

import com.mohammed.timetracker.model.Task;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.repository.TaskRepository;
import com.mohammed.timetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + creatorUsername));
        task.setCreatedBy(creator);
        task.setStatus(task.getStatus() != null ? task.getStatus() : com.mohammed.timetracker.model.TaskStatus.TODO);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getMyTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return taskRepository.findByAssignedTo(user);
    }

    public Task updateTask(Long id, Task updated) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updated.getTitle());
            task.setDescription(updated.getDescription());
            task.setStatus(updated.getStatus());
            if (updated.getPriority() != null) task.setPriority(updated.getPriority());
            if (updated.getDueDate() != null) task.setDueDate(updated.getDueDate());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public Task assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        task.setAssignedTo(user);
        return taskRepository.save(task);
    }

    public Task updateStatus(Long id, String status, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            throw new RuntimeException("You are not assigned to this task");
        }
        task.setStatus(com.mohammed.timetracker.model.TaskStatus.valueOf(status));
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}

