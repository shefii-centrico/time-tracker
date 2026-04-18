package com.mohammed.timetracker.service;

import com.mohammed.timetracker.dto.CreateProjectRequest;
import com.mohammed.timetracker.model.Project;
import com.mohammed.timetracker.model.ProjectAssignment;
import com.mohammed.timetracker.model.ProjectStatus;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.repository.ProjectAssignmentRepository;
import com.mohammed.timetracker.repository.ProjectRepository;
import com.mohammed.timetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectAssignmentRepository assignmentRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
    }

    public Project create(CreateProjectRequest request, String createdByUsername) {
        User creator = userRepository.findByUsername(createdByUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + createdByUsername));
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setClientName(request.getClientName());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(ProjectStatus.ACTIVE);
        project.setCreatedBy(creator);
        return projectRepository.save(project);
    }

    public Project updateStatus(Long id, String status) {
        Project project = getById(id);
        project.setStatus(ProjectStatus.valueOf(status));
        return projectRepository.save(project);
    }

    public ProjectAssignment assignUser(Long projectId, Long userId, String roleInProject) {
        Project project = getById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        // Upsert: if already assigned, update role
        ProjectAssignment assignment = assignmentRepository.findByProjectAndUser(project, user)
                .orElse(new ProjectAssignment());
        assignment.setProject(project);
        assignment.setUser(user);
        assignment.setRoleInProject(roleInProject);
        return assignmentRepository.save(assignment);
    }

    public void removeAssignment(Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    public List<ProjectAssignment> getAssignments(Long projectId) {
        Project project = getById(projectId);
        return assignmentRepository.findByProject(project);
    }

    public List<ProjectAssignment> getMyProjects(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return assignmentRepository.findByUser(user);
    }
}
