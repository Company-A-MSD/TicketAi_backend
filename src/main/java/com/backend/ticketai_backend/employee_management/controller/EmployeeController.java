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
import com.backend.ticketai_backend.util.JwtUtil;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginData) {
        // Authentication logic (mocked)
        String email = loginData.getEmail();
        String password = loginData.getPassword();
        // For demonstration, assuming a successful login
        String token = jwtUtil.generateToken(email, "ADMIN", "emp_01");
        return ResponseEntity.ok(Map.of("token", token));
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

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getAllEmployees() {
        List<Map<String, String>> employees = List.of(
            Map.of("employee_id", "emp_01", "name", "Jane Smith", "email", "jane@example.com", "role", "Support Agent", "status", "available"),
            Map.of("employee_id", "emp_02", "name", "John Doe", "email", "john@example.com", "role", "Support Agent", "status", "busy")
        );
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable String id) {
        Map<String, Object> employee = Map.of(
            "employee_id", id,
            "name", "John Doe",
            "email", "john@example.com",
            "role", "Support Agent",
            "assigned_categories", List.of("Billing", "Technical"),
            "status", "available"
        );
        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateEmployee(@PathVariable String id, @RequestBody Map<String, Object> employeeData) {
        // Mock update logic
        return ResponseEntity.ok(Map.of("message", "Employee updated successfully", "employee_id", id));
    }
}
