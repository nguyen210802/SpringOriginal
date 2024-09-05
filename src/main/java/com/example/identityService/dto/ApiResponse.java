package com.example.identityService.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.protocol.types.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    @Builder.Default
    int code = 1000;
    String message;
    T result;
}
