package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String message;
    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
