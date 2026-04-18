package com.mohammed.timetracker.repository;

import com.mohammed.timetracker.model.Project;
import com.mohammed.timetracker.model.ProjectAssignment;
import com.mohammed.timetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    List<ProjectAssignment> findByProject(Project project);
    List<ProjectAssignment> findByUser(User user);
    Optional<ProjectAssignment> findByProjectAndUser(Project project, User user);
}
