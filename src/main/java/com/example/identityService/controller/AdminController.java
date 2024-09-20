package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Order;
import com.example.identityService.service.AdminService;
import com.example.identityService.service.OrderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    Map<String, Object> map;

    public AdminController(AdminService adminServiceImpl, OrderService orderServiceImpl) {
        this.map = Map.of("admin", adminServiceImpl, "order", orderServiceImpl);
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<UserResponse>> getUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        AdminService adminService = (AdminService)this.map.get("admin");

        return ApiResponse.<PageResponse<UserResponse>>builder()
                .result(adminService.getAll(page, size))
                .build();
    }

    @GetMapping()
    public ApiResponse<UserResponse> getUser(@RequestParam String id){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<UserResponse>builder()
                .result(adminService.getUserById(id))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestParam String id, @RequestBody UserRequest request){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<UserResponse>builder()
                .result(adminService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUser(@RequestParam String id){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<String>builder()
                .result(adminService.deleteUser(id))
                .build();
    }

    @PutMapping("order/delivery")
    public ApiResponse<Order> deliveryOrder(@RequestParam String orderId){
        OrderService orderService = (OrderService)this.map.get("order");
        return ApiResponse.<Order>builder()
               .result(orderService.successfullyDelivery(orderId))
               .build();
    }
}
