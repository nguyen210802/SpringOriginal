package com.example.identityService.service;

import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getMyInfo();
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(UserRequest request);
    String deleteUser();
}
