package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Product;
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

    public UserController(UserService userServiceImpl) {
        this.map = Map.of("user", userServiceImpl);
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getCurrentUser() {
        UserService userService = (UserService) map.get("user");
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
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

    //
    //
    //
    //Product
    //
    //
    //

    @GetMapping("/product/getAll")
    public ApiResponse<List<Product>> getAll(){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<List<Product>>builder()
                .result(productService.getAll())
                .build();
    }

    @GetMapping("/product")
    public ApiResponse<Product> getProductById(@RequestParam String id){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @PostMapping("/product/create")
    public ApiResponse<Product> createProduct(@RequestBody Product product){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.create(product))
                .build();
    }

    @PutMapping("/product/update")
    public ApiResponse<Product> updateProduct(@RequestParam String id, @RequestBody Product product){
        log.info("Product update Controller");
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.update(id, product))
                .build();
    }

    @DeleteMapping("/product/delete")
    public ApiResponse<String> deleteProduct(@RequestParam String id){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<String>builder()
                .result(productService.delete(id))
                .build();
    }
}
