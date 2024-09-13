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
import com.example.identityService.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    UserMapper userMapper;
    UserRepository userRepository;
    CartRepository cartRepository;
    PasswordEncoder passwordEncoder;
    KafkaTemplate<String, Object> kafkaTemplate;


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
    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        try {
            user = userRepository.save(user);
        }catch (Exception e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Cart cart = Cart.builder()
                .buyer(user)
                .build();
        cartRepository.save(cart);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .build();
//
        kafkaTemplate.send("notification-createUser", notificationEvent);

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
}
