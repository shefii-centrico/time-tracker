package com.mohammed.timetracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "time_logs")
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;
    private Double hours;
    private LocalDate date;

    public TimeLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Double getHours() { return hours; }
    public void setHours(Double hours) { this.hours = hours; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
