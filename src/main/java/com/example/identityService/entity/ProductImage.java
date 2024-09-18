package com.example.identityService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(length = 1000000)
    byte[] image;

    boolean isMainImage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    Product product;
}
