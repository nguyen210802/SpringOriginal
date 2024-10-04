package com.example.identityService.repository;

import com.example.identityService.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllByUser_Id(String userId, Pageable pageable);
    long countByReadFalseAndUser_Id(String userId);
}
