package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.service.AdminService;
import com.example.identityService.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
    public ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(map.get("admin").getAll())
                .build();
//        return map.get("admin").getAll();
    }

    @GetMapping()
    public ApiResponse<UserResponse> getUser(@RequestParam String id){
        return ApiResponse.<UserResponse>builder()
                .result(map.get("admin").getUserById(id))
                .build();
//        return map.get("admin").getUserById(id);
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestParam String id, @RequestBody UserRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(map.get("admin").updateUser(id, request))
                .build();
//        return map.get("admin").updateUser(id, request);
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUser(@RequestParam String id){
        return ApiResponse.<String>builder()
                .result(map.get("admin").deleteUser(id))
                .build();
//        return map.get("admin").deleteUser(id);
    }
}
