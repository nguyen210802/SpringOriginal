package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToOne
    @JoinColumn(nullable = false)
    Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    List<OrderItem> orderItems;

    @Column(nullable = false)
    double totalAmount;

    @Column(nullable = false)
    @Builder.Default
    boolean delivery = false;

    @Column(unique = true, nullable = false)
    LocalDate createAt;
    LocalDate updateAt;

    @PrePersist
    private void setCreateAt(){
        this.createAt = LocalDate.now();
    }

    @PreUpdate
    private void setUpdateAt(){
        this.updateAt = LocalDate.now();
    }
}
