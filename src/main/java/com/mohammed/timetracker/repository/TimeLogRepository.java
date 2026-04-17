package com.mohammed.timetracker.repository;

import com.mohammed.timetracker.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    List<TimeLog> findByTaskId(Long taskId);
}
