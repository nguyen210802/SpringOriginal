package com.example.identityService.entity;

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
//    @JsonIgnore
    List<ProductImage> images;

    String description;
    String manufacturer;

    @Column(nullable = false)
    double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    List<Comment> comments;

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
