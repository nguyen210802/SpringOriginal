package com.example.identityService.service;

public interface OtpService {
    String generateOTP(String username);
    boolean validateOTP(String username, String otp);
    void clearOTP(String username);
}
