package com.mohammed.timetracker.repository;

import com.mohammed.timetracker.model.Task;
import com.mohammed.timetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
}

