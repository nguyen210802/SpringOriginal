package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Cart;
import com.example.identityService.entity.Product;
import com.example.identityService.service.CartService;
import com.example.identityService.service.ProductService;
import com.example.identityService.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    Map<String, Object> map;

    public UserController(UserService userServiceImpl, CartService cartServiceImpl) {
        this.map = Map.of("user", userServiceImpl, "cart", cartServiceImpl);
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getCurrentUser() {
        UserService userService = (UserService) map.get("user");
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/myCart")
    public ApiResponse<Cart> getMyCart(){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Cart>builder()
               .result(cartService.getMyCart())
               .build();
    }

    @GetMapping("/myCart/count")
    public ApiResponse<Integer> getCountMyCart(){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Integer>builder()
                .result(cartService.countMyCart())
                .build();
    }

    @PostMapping("/registration")
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        UserService userService = (UserService) map.get("user");

        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        UserService userService = (UserService) map.get("user");

        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userRequest))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUser() {
        UserService userService = (UserService) map.get("user");

        return ApiResponse.<String>builder()
                .result(userService.deleteUser())
                .build();
    }
}
