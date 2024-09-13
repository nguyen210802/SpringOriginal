package com.example.identityService.controller;

import com.example.identityService.dto.NotificationEvent;
import com.example.identityService.dto.request.Email;
import com.example.identityService.dto.request.SendEmailRequest;
import com.example.identityService.repository.httpclient.EmailClient;
import com.example.identityService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationServiceImpl;

    @PostMapping("/sendEmail")
    public void sendEmail(){
        notificationServiceImpl.sendEmail(
                SendEmailRequest.builder()
                        .to(Email.builder()
                                .email("nguyen1@Yopmail.com")
                                .name("NGUYEN")
                                .build())
                        .subject("Xin chao")
                        .htmlContent("<p>Chao mung den voi ngoi nha cua Nguyen</p>")
                        .build());
    }

    @KafkaListener(topics = "notification-createUser")
    public void listNotifications(NotificationEvent message){
        log.info("Message received: {}", message);
        notificationServiceImpl.sendEmail(
                SendEmailRequest.builder()
                        .to(Email.builder()
                                .email(message.getEmail())
                                .name("NGUYEN")
                                .build())
                        .subject(message.getSubject())
                        .htmlContent(message.getHtmlContent())
                .build());
    }
}
