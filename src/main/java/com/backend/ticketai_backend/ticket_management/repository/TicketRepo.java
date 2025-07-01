package com.backend.ticketai_backend.ticket_management.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.ticketai_backend.ticket_management.model.Ticket;

public interface TicketRepo extends MongoRepository<Ticket, String> {
    List<Ticket> findByCreated(String created);
    List<Ticket> findByAssigned(String assigned);
}
