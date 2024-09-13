package com.example.identityService.entity;

import com.example.identityService.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Enumerated(EnumType.STRING)
    Role role;

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Cart cart;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Product> products;
//    Date createAt;
//    Date updateAt;
//
//    @PrePersist
//    public void setCreateAt(){
//        this.createAt = new Date();
//        this.updateAt = new Date();
//    }
//
//    @PreUpdate
//    public void setUpdateAt(){
//        this.updateAt = new Date();
//    }
}
