package com.mohammed.timetracker.repository;

import com.mohammed.timetracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
