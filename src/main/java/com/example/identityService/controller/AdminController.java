package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.service.AdminService;
import com.example.identityService.service.UserService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    Map<String, AdminService> map;

    public AdminController(AdminService adminServiceImpl) {
        this.map = Map.of("admin", adminServiceImpl);
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<UserResponse>> getUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .result(map.get("admin").getAll(page, size))
                .build();
    }

    @GetMapping()
    public ApiResponse<UserResponse> getUser(@RequestParam String id){
        return ApiResponse.<UserResponse>builder()
                .result(map.get("admin").getUserById(id))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestParam String id, @RequestBody UserRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(map.get("admin").updateUser(id, request))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUser(@RequestParam String id){
        return ApiResponse.<String>builder()
                .result(map.get("admin").deleteUser(id))
                .build();
    }
}
