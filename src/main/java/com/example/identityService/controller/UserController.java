package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Cart;
import com.example.identityService.service.CartService;
import com.example.identityService.service.OtpService;
import com.example.identityService.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    Map<String, Object> map;

    public UserController(UserService userServiceImpl, CartService cartServiceImpl, OtpService otpServiceImpl) {
        this.map = Map.of("user", userServiceImpl, "cart", cartServiceImpl, "otp", otpServiceImpl);
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

    @PostMapping("/registration")
    public ApiResponse<String> createUser(@RequestBody UserRequest request) {
        UserService userService = (UserService) map.get("user");

        return ApiResponse.<String>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostMapping("/confirmOtpAndCreateUser")
    public ApiResponse<UserResponse> confirmOtpAndCreateUser(@RequestBody UserRequest request, @RequestParam String otp) {
        UserService userService = (UserService) map.get("user");

        return ApiResponse.<UserResponse>builder()
                .result(userService.confirmOtpAndCreateUser(request, otp))
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
