package com.backend.ticketai_backend.employee_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.backend.ticketai_backend.notification.MailDetailsDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.backend.ticketai_backend.employee_management.dto.LoginRequestDto;
import com.backend.ticketai_backend.employee_management.model.Employee;
import com.backend.ticketai_backend.employee_management.service.EmployeeService;
import com.backend.ticketai_backend.util.JwtUtil;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    // 1. LOGIN

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginData) {
        return employeeService.login(loginData.getEmail(), loginData.getPassword())
                .map(emp -> {
                    String token = jwtUtil.generateToken(emp.getEmail(), emp.getRole(), emp.get_id());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("message", "Invalid credentials")));
    }


    @PatchMapping("/status_update/{id}")
    public ResponseEntity<Map<String, String>> updateStatus(@PathVariable String id, @RequestBody Map<String, String> statusData) {
        String status = statusData.get("status");
        String toEmail = statusData.get("toEmail");      // Email of employee to notify
        String subject = statusData.get("subject");      // Email subject
        String message = statusData.get("message");      // Email message

        // Create DTO from input data
        MailDetailsDTO mailDetailsDTO = new MailDetailsDTO(toEmail, message, subject);

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(mailDetailsDTO.getToMail());
            email.setFrom("sahanudayangaof@gmail.com");
            email.setSubject(mailDetailsDTO.getSubject());
            email.setText(mailDetailsDTO.getMessage());
            javaMailSender.send(email);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Status updated, but failed to send email",
                    "employee_id", id,
                    "status", status,
                    "error", e.getMessage()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Status updated and confirmation email sent",
                "employee_id", id,
                "status", status
        ));
  }


    // 3. GET ALL EMPLOYEES
    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<Map<String, Object>> result = employees.stream().map(emp -> Map.of(
                "employee_id", (Object) emp.get_id(),
                "name", (Object) emp.getName(),
                "email", (Object) emp.getEmail(),
                "role", (Object) emp.getRole(),
                "availability", (Object) emp.getAvailability(),
                "assigned_categories", (Object) emp.getAssigned_categories(),
                "workload", (Object) emp.getWorkload()
        )).toList();
        return ResponseEntity.ok(result);
    }

    // 4. GET EMPLOYEE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id)
                .map(emp -> Map.of(
                        "employee_id", emp.get_id(),
                        "name", emp.getName(),
                        "email", emp.getEmail(),
                        "role", emp.getRole(),
                        "assigned_categories", emp.getAssigned_categories(),
                        "availability", emp.getAvailability(),
                        "workload",emp.getWorkload()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. UPDATE EMPLOYEE DETAILS
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody Employee updatedData) {
        Employee updated = employeeService.updateEmployee(id, updatedData);
        return ResponseEntity.ok(Map.of("message", "Employee updated successfully", "employee_id", updated.get_id()));
    }
}