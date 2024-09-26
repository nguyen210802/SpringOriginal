package com.example.identityService.service.impl;

import com.example.identityService.dto.NotificationEvent;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Cart;
import com.example.identityService.entity.User;
import com.example.identityService.enums.Role;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.mapper.UserMapper;
import com.example.identityService.repository.CartRepository;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.OtpService;
import com.example.identityService.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    CartRepository cartRepository;
    PasswordEncoder passwordEncoder;
    KafkaTemplate<String, Object> kafkaTemplate;
    OtpService otpService;


    @Override
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        return userMapper.toUserResponse(user);
    }

    @Override
    public String createUser(UserRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null || userRepository.findByEmail(request.getEmail()) != null)
            throw new AppException(ErrorCode.USER_EXISTED);

        sendEmail(request.getUsername(), request.getEmail());
        return "Send Email Successfully";
    }

    @Override
    @Transactional
    public UserResponse confirmOtpAndCreateUser(UserRequest request, String otp) {
        if(userRepository.findByUsername(request.getUsername()) != null || userRepository.findByEmail(request.getEmail()) != null)
            throw new AppException(ErrorCode.USER_EXISTED);
        log.info("Authentication OTP: {}", !otpService.validateOTP(request.getUsername(), otp));
        if(!otpService.validateOTP(request.getUsername(), otp))
            throw new RuntimeException("Invalid");

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        Cart cart = Cart.builder()
                .buyer(user)
                .build();
        cartRepository.save(cart);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = authentication.getName();

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public String deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = authentication.getName();
        userRepository.deleteById(id);

        return "Delete successfully user";
    }

    public String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    private void sendEmail(String username, String email){
        String otp = otpService.generateOTP(username);
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .email(email)
                .subject("CHAO MUNG BAN DEN VOI NGOI NHA CUA NGUYEN")
                .htmlContent("<P>Ban da dang ky thanh cong tai khoan cua nguyen123.vn.<br> Ma xac thuc cua ban la:" + otp +" <br>Chuc ban co nhung thoi gian vui ve!</p>")
                .build();
        try {
            kafkaTemplate.send("notification-createUser", notificationEvent);
        }catch (Exception e){
            throw new RuntimeException("Loi khi gui email");
        }
    }
}
