package com.example.identityService.service;

import com.example.identityService.dto.request.SendEmailRequest;

public interface NotificationService {
    void sendEmail(SendEmailRequest request);
}
