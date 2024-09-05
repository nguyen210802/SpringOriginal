package com.example.identityService.service;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;

import java.util.List;

public interface AdminService{
    List<UserResponse> getAll();
    UserResponse getUserById(String id);
    UserResponse updateUser(String id, UserRequest request);
    String deleteUser(String id);
}
