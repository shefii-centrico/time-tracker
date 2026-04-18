package com.mohammed.timetracker.service;

import com.mohammed.timetracker.dto.CreateDepartmentRequest;
import com.mohammed.timetracker.model.Department;
import com.mohammed.timetracker.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public Department create(CreateDepartmentRequest request) {
        if (departmentRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Department already exists: " + request.getName());
        }
        Department dept = new Department();
        dept.setName(request.getName());
        dept.setDescription(request.getDescription());
        return departmentRepository.save(dept);
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
