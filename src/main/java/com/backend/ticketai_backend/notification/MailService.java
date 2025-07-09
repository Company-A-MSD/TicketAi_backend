package com.backend.ticketai_backend.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService 
{
    @Autowired
    private JavaMailSender javaMailSender;

    public Boolean sendEmail(MailDetailsDTO mailDetailsDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(mailDetailsDTO.getSubject());
            message.setTo(mailDetailsDTO.getToMail());
            message.setFrom("abcs@gmail.com");
            message.setText(mailDetailsDTO.getMessage());

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
