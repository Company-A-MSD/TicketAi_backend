package com.backend.ticketai_backend.ticket_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTicketStatusDto {

    private String status;
    private String assigned_employee_email;

}
