package com.mohammed.timetracker.repository;

import com.mohammed.timetracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
