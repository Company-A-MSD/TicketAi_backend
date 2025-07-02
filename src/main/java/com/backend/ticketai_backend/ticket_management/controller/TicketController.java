package com.backend.ticketai_backend.ticket_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.ticketai_backend.aiservice.service.ChatService;
import com.backend.ticketai_backend.employee_management.model.Employee;
import com.backend.ticketai_backend.employee_management.service.EmployeeService;
import com.backend.ticketai_backend.ticket_management.dto.CreateTicketRequestDto;
import com.backend.ticketai_backend.ticket_management.dto.UpdateTicketStatusDto;
import com.backend.ticketai_backend.ticket_management.model.Ticket;
import com.backend.ticketai_backend.ticket_management.service.TicketService;
import com.backend.ticketai_backend.util.JwtUtil;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/tickets")
public class TicketController 
{
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ChatService chatService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/")
    public ResponseEntity<?> CreateTicket(@RequestBody CreateTicketRequestDto ticketrequest,@RequestHeader("Authorization") String token){
       
        Claims claims = jwtUtil.getClaimsFromToken(token.substring(7));
        String userId = claims.get("id", String.class);

        // Call the Aiservice to determine the category of the ticket
        String category = chatService.generate(ticketrequest.getDescription());
        Employee employee = employeeService.TicketAssignmet(category);
        if (employee == null) {
            return ResponseEntity.ok(Map.of("message", "No employee available for this category","success", false));
        }
        // Create a new ticket object
        Ticket ticket = new Ticket();
        ticket.setSubject(ticketrequest.getSubject());
        ticket.setDescription(ticketrequest.getDescription());
        ticket.setCategory(category);
        ticket.setCreated_by(userId);
        ticket.setAssigned_to(employee.getName());

        Ticket createdTicket = ticketService.createTicket(ticket);
        if (createdTicket == null) {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to create ticket"));
        }
        return ResponseEntity.ok(Map.of("message", "Ticket created successfully"));
    }

    @GetMapping("/")
    public ResponseEntity<?> getTicketssofUser(@RequestHeader("Authorization") String token){
        Claims claims = jwtUtil.getClaimsFromToken(token.substring(7));
        String userId = claims.get("id", String.class);
        
        // Call the service to get tickets by user ID
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        // Convert the tickets to a suitable response format
        if(tickets.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("message", "No tickets found for this user"));
        }

        List<Map<String, Object>> result = tickets.stream().map(ticket -> Map.of(
                "ticket_id", (Object)  ticket.get_id(),
                "title", (Object) ticket.getSubject(),
                "description", (Object) ticket.getDescription(),
                "status", (Object) ticket.getStatus(),
                "category", (Object) ticket.getCategory(),
                "assigned_to", (Object) ticket.getAssigned_to(),
                "created_at", (Object) ticket.getCreated_at()
        )).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTicket(@PathVariable String id) {
        // Call the service to get ticket by ID
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket != null) {
            return ResponseEntity.ok(Map.of(
                    "ticket_id", (Object) ticket.get_id(),
                    "title", (Object) ticket.getSubject(),
                    "description", (Object) ticket.getDescription(),
                    "status", (Object) ticket.getStatus(),
                    "category", (Object) ticket.getCategory(),
                    "assigned_to", (Object) ticket.getAssigned_to(),
                    "created_at", (Object) ticket.getCreated_at()
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Ticket not found"));
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateTicket(@RequestParam String id,@RequestBody UpdateTicketStatusDto updateTicketStatusDto) {

        Ticket ticket = ticketService.UpdateTicket(id, updateTicketStatusDto);
        if (ticket == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Ticket not found"));
        }
        return ResponseEntity.ok(Map.of("message", "Ticket updated successfully"));
    }   
    
    @GetMapping("/assigned")
    public ResponseEntity<?> getAssignedTickets(@RequestHeader("Authorization") String token) {
        Claims claims = jwtUtil.getClaimsFromToken(token.substring(7));
        String empId = claims.get("id", String.class);

        List<Ticket> tickets = ticketService.getAssignedTickets(empId);
        if (tickets.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No assigned tickets found"));
        }

        return ResponseEntity.ok(
            tickets.stream().map(ticket -> {
                
                return Map.of(
                    "ticket_id", ticket.get_id(),
                    "title", ticket.getSubject(),
                    "description", ticket.getDescription(),
                    "status", ticket.getStatus(),
                    "category", ticket.getCategory(),
                    "assigned_to", ticket.getAssigned_to(),
                    "created_by", ticket.getCreated_by(),
                    "created_at", ticket.getCreated_at()
                );
            }).toList()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTickets() {

       List<Ticket> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No tickets found"));
        }
       
       return ResponseEntity.ok(
            tickets.stream().map(ticket -> {
                
                return Map.of(
                    "ticket_id", ticket.get_id(),
                    "title", ticket.getSubject(),
                    "description", ticket.getDescription(),
                    "status", ticket.getStatus(),
                    "category", ticket.getCategory(),
                    "assigned_to", ticket.getAssigned_to(),
                    "created_at", ticket.getCreated_at(),
                    "created_by", ticket.getCreated_by()
                );
            }).toList()
        );
    }
    
    
    
}
