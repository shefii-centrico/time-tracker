package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.dto.CreateProjectRequest;
import com.mohammed.timetracker.model.Project;
import com.mohammed.timetracker.model.ProjectAssignment;
import com.mohammed.timetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_LEAD')")
    public List<Project> getAll() {
        return projectService.getAll();
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public List<ProjectAssignment> getMyProjects(Authentication auth) {
        return projectService.getMyProjects(auth.getName());
    }

    @GetMapping("/{id}/assignments")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public List<ProjectAssignment> getAssignments(@PathVariable Long id) {
        return projectService.getAssignments(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public Project create(@Valid @RequestBody CreateProjectRequest request, Authentication auth) {
        return projectService.create(request, auth.getName());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public Project updateStatus(@PathVariable Long id, @RequestParam String status) {
        return projectService.updateStatus(id, status);
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ProjectAssignment assignUser(@PathVariable Long id,
                                        @RequestParam Long userId,
                                        @RequestParam(required = false, defaultValue = "EMPLOYEE") String roleInProject) {
        return projectService.assignUser(id, userId, roleInProject);
    }

    @DeleteMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void removeAssignment(@PathVariable Long assignmentId) {
        projectService.removeAssignment(assignmentId);
    }
}
