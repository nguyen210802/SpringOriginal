package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
//    @JsonIgnore
    User buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    Set<OrderItem> orderItems;

    @Column(nullable = false)
    double totalAmount;

    @Column(nullable = false)
    LocalDateTime createAt;

    LocalDateTime updateAt;

    @PrePersist
    private void SetOrderDate(){
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    private void SetUpdateAt(){
        this.updateAt = LocalDateTime.now();
    }
}
