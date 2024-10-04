package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    String message;

    @Column(nullable = false)
    boolean read;
    LocalDate createAt;

    @PrePersist
    private void setCreateAt(){
        this.createAt = LocalDate.now();
    }
}
