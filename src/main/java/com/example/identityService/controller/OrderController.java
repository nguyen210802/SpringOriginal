package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.OrderItem;
import com.example.identityService.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<Order>> getAll(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<PageResponse<Order>>builder()
               .result(orderService.getAll(page, size))
               .build();
    }

    @GetMapping()
    public ApiResponse<Order> getOrder(@RequestParam String orderId){
        return ApiResponse.<Order>builder()
                .result(orderService.getOrder(orderId))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<Order> create(@RequestParam String addressId, @RequestBody Set<OrderItem> orderItems){
        return ApiResponse.<Order>builder()
                .result(orderService.createOrder(addressId, orderItems))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteOrder(@RequestParam String orderId){
        return ApiResponse.<String>builder()
                .result(orderService.deleteOrder(orderId))
                .build();
    }
}
