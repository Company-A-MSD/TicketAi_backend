package com.backend.ticketai_backend.ticket_management.service;

import java.sql.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.ticketai_backend.employee_management.model.Employee;
import com.backend.ticketai_backend.employee_management.service.EmployeeService;
import com.backend.ticketai_backend.notification.MailDetailsDTO;
import com.backend.ticketai_backend.notification.MailService;
import com.backend.ticketai_backend.ticket_management.dto.UpdateTicketStatusDto;
import com.backend.ticketai_backend.ticket_management.model.Ticket;
import com.backend.ticketai_backend.ticket_management.repository.TicketRepo;

@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MailService mailService;

    public List<Ticket> getTicketsByUserId(ObjectId userId) {
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
            if(updateTicketStatusDto.getStatus()!=null)
            {
                ticket.setStatus(updateTicketStatusDto.getStatus());
                if (updateTicketStatusDto.getStatus().equals("Complete")) 
                {
                    MailDetailsDTO mailDetailsDTO = new MailDetailsDTO();
                    mailDetailsDTO.setMessage("Your ticket with ID " + ticketId + " has been marked as complete.");
                    mailDetailsDTO.setSubject("Ticket Status Update");
                    mailDetailsDTO.setToMail(ticket.getCreated_by().toString());
                    mailService.sendEmail(mailDetailsDTO);
                }
            }
            if(updateTicketStatusDto.getAssigned_employee_email()!=null)
            {
                Employee emp =employeeService.getEmployeebyEmail(updateTicketStatusDto.getAssigned_employee_email());
                if(emp!=null)
                {
                    ticket.setAssigned_to(new ObjectId(emp.get_id()));
                }
            }
            ticket.setUpdated_at(new Date(System.currentTimeMillis()));
            return ticketRepo.save(ticket);
        }
        return null;
    }

    public List<Ticket> getAssignedTickets(ObjectId empId)
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
