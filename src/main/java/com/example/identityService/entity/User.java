package com.example.identityService.entity;

import com.example.identityService.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    @Column(name = "username", unique = true)
    String username;
    String password;
    @Column(name = "email", unique = true)
    String email;
    Role role;
    Date createAt;
    Date updateAt;

    @PrePersist
    public void setCreateAt(){
        this.createAt = new Date();
        this.updateAt = new Date();
    }

    @PreUpdate
    public void setUpdateAt(){
        this.updateAt = new Date();
    }
}
