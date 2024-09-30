package com.example.identityService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    fetch = FetchType.LAZY
    List<ProductImage> images;

    String description;
    String manufacturer;

    @Column(nullable = false)
    double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Comment> comments;

    @Column(nullable = false)
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
