package com.example.identityService.service.impl;

import com.example.identityService.service.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OtpServiceImpl implements OtpService {
    private Map<String, String> otpStore = new HashMap<>();

    @Override
    public String generateOTP(String username) {
        // Sinh OTP ngẫu nhiên (ví dụ: 6 chữ số)
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        // Lưu OTP với tên người dùng
        otpStore.put(username, otp);  // Đảm bảo username là một chuỗi đơn giản

        log.info("otpStore: {}", otpStore);
        log.info("Otp: {}", otp);

        return otp;
    }

    @Override
    public boolean validateOTP(String username, String otp) {
        log.info("usernameRequest: {}", username);
        log.info("otpRequest: {}", otp);
        String storedOtp = otpStore.get(username);
        log.info("storedOtp: {}", storedOtp);
        return storedOtp != null && storedOtp.equals(otp);
    }

    private void sendOTPToUser(String username, String otp) {
        // Thực hiện logic gửi OTP cho người dùng qua email/SMS
        System.out.println("Sending OTP: " + otp + " to user: " + username);
    }

    @Override
    public void clearOTP(String username) {
        otpStore.remove(username);
    }
}
