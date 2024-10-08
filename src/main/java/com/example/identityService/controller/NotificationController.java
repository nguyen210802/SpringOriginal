package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.NotificationEvent;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.Email;
import com.example.identityService.dto.request.SendEmailRequest;
import com.example.identityService.entity.Notification;
import com.example.identityService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/notifications")
    public ApiResponse<PageResponse<Notification>> getAllByUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<Notification>>builder()
                .result(notificationServiceImpl.getAllMyNotification(page, size))
                .build();
    }

    @GetMapping("/notifications/countReadFalse")
    public ApiResponse<Long> getNotificationReadFalse(){
        return ApiResponse.<Long>builder()
                .result(notificationServiceImpl.NotificationCount())
                .build();
    }

    @PutMapping("/notifications/read")
    public ApiResponse<Notification> readNotification(@RequestBody String notificationId){
        return ApiResponse.<Notification>builder()
               .result(notificationServiceImpl.readNotification(notificationId))
               .build();
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
