package com.example.identityService.service.impl;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.Email;
import com.example.identityService.dto.request.EmailRequest;
import com.example.identityService.dto.request.SendEmailRequest;
import com.example.identityService.entity.Notification;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.NotificationRepository;
import com.example.identityService.repository.httpclient.EmailClient;
import com.example.identityService.service.NotificationService;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
    EmailClient emailClient;
    NotificationRepository notificationRepository;

    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;

    @Override
    public void sendEmail(SendEmailRequest request) {
//        Email email = Email.builder().email("nguyen1@yopmail.com").name("Nguyen").build();
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

    @Override
    public PageResponse<Notification> getAllMyNotification(int page, int size) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String userId = authenticated.getName();

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,"createAt"));
        var pageData = notificationRepository.findAllByUser_Id(userId, pageable);

        return PageResponse.<Notification>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public Notification readNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new RuntimeException("Notification not found")
        );
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public long NotificationCount() {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String userId = authenticated.getName();
        return notificationRepository.countByReadFalseAndUser_Id(userId);
    }
}
