package com.example.identityService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     String id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @Column(nullable = false)
    String name;

    @Lob
    byte[] image;

    String description;
    String manufacturer;

    @Column(nullable = false)
    double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Comment> comments;

    @Column(nullable = false)
    LocalDateTime createAt;
    LocalDateTime updateAt;

    @PrePersist
    private void setCreateAt(){
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setUpdateAt(){
        this.updateAt = LocalDateTime.now();
    }
}
