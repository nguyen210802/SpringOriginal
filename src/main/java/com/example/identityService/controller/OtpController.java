package com.example.identityService.controller;

import com.example.identityService.service.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OtpController {
    OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestParam String username) {
        String otp = otpService.generateOTP(username);
        return ResponseEntity.ok("OTP has been sent to your email/SMS.");
    }

    // API để xác nhận OTP
    @PostMapping("/validate")
    public ResponseEntity<String> validateOTP(@RequestParam String username, @RequestParam String otp) {

        if (otpService.validateOTP(username, otp)) {
            otpService.clearOTP(username); // Xóa OTP sau khi xác nhận
            return ResponseEntity.ok("OTP is valid!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP!");
        }
    }
}
