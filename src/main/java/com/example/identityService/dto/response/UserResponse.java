package com.example.identityService.dto.response;

import com.example.identityService.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password;
    String email;
    Role role;
    String phone;
    String dob;
    @Column(unique = true, nullable = false)
    LocalDate createAt;
    LocalDate updateAt;
}
