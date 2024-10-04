package com.example.identityService.service;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.SendEmailRequest;
import com.example.identityService.entity.Notification;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.Product;

public interface NotificationService {
    void sendEmail(SendEmailRequest request);
    PageResponse<Notification> getAllMyNotification(int page, int size);
    Notification readNotification(String notificationId);
    long NotificationCount();
}
