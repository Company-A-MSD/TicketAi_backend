package com.backend.ticketai_backend.ticket_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.ticketai_backend.ticket_management.dto.UpdateTicketStatusDto;
import com.backend.ticketai_backend.ticket_management.model.Ticket;
import com.backend.ticketai_backend.ticket_management.repository.TicketRepo;

@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepo;

    public List<Ticket> getTicketsByUserId(String userId) {
        // Logic to fetch tickets by user ID

        List<Ticket> results = ticketRepo.findByCreated(userId);
        return results;
    }

    public Ticket getTicketById(String ticketId) {
        // Logic to fetch a ticket by its ID
        return ticketRepo.findById(ticketId).orElse(null);
    }

    public Ticket UpdateTicket(String ticketId, UpdateTicketStatusDto updateTicketStatusDto) {
        // Logic to update a ticket's status
        Ticket ticket = ticketRepo.findById(ticketId).orElse(null);
        if (ticket != null) {
            ticket.setStatus(updateTicketStatusDto.getStatus());
            ticket.setAssigned_to(updateTicketStatusDto.getAssigned_employee_email());
            return ticketRepo.save(ticket);
        }
        return null;
    }

    public List<Ticket> getAssignedTickets(String empId)
    {
        return ticketRepo.findByAssigned(empId);
    }

    public List<Ticket> getAllTickets() {
        // Logic to fetch all tickets
        return ticketRepo.findAll();
    }

    public Ticket createTicket(Ticket ticket) {
        // Logic to create a new ticket
        return ticketRepo.save(ticket);
    }

}
