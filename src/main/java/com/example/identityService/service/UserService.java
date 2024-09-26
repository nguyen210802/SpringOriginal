package com.example.identityService.service;

import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getMyInfo();
    String createUser(UserRequest userRequest);
    UserResponse confirmOtpAndCreateUser(UserRequest request, String otp);
    UserResponse updateUser(UserRequest request);
    String deleteUser();
}
