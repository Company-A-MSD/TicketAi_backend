package com.backend.ticketai_backend.employee_management.repository;

import com.backend.ticketai_backend.employee_management.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Employee findByEmail(String email);
    List<Employee> findByAssignedCategories(String category);
}
