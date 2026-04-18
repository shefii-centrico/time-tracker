package com.mohammed.timetracker.repository;

import com.mohammed.timetracker.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    List<TimeLog> findByTask_Id(Long taskId);
    List<TimeLog> findByUser_Username(String username);
    List<TimeLog> findByUser_Id(Long userId);
    List<TimeLog> findByDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT t FROM TimeLog t WHERE " +
           "(:taskId IS NULL OR t.task.id = :taskId) AND " +
           "(:userId IS NULL OR t.user.id = :userId) AND " +
           "(:startDate IS NULL OR t.date >= :startDate) AND " +
           "(:endDate IS NULL OR t.date <= :endDate) " +
           "ORDER BY t.date DESC, t.id DESC")
    List<TimeLog> findFiltered(
        @Param("taskId") Long taskId,
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
