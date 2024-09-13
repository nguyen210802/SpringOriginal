package com.example.identityService.service.impl;

import com.example.identityService.dto.request.Email;
import com.example.identityService.dto.request.EmailRequest;
import com.example.identityService.dto.request.SendEmailRequest;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.httpclient.EmailClient;
import com.example.identityService.service.NotificationService;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
    EmailClient emailClient;

    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;

    @Override
    public void sendEmail(SendEmailRequest request) {
        Email email = Email.builder().email("nguyen1@yopmail.com").name("Nguyen").build();
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Email.builder()
                        .email("blackgenshin123@gmail.com")
                        .name("TRAN QUANG NGUYEN")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            emailClient.sendEmail(apiKey, emailRequest);
        }catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
