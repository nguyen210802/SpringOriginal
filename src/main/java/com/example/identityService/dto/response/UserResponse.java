package com.example.identityService.dto.response;

import com.example.identityService.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
