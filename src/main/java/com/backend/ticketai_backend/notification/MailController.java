package com.backend.ticketai_backend.notification;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notifications")
public class MailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/")
    public String sendEmail(@RequestBody MailDetailsDTO mailDetailsDTO) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(mailDetailsDTO.getSubject());
            message.setTo(mailDetailsDTO.getToMail());
            message.setFrom("sahanudayangaof@gmail.com");
            message.setText(mailDetailsDTO.getMessage());


            javaMailSender.send(message);
            return "success";

        } catch (Exception e) {
            return e.getMessage();
        }

    }

}
