package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.entity.Cart;
import com.example.identityService.service.CartService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {
    Map<String, Object> map;

    public CartController(CartService cartServiceImpl) {
        this.map = Map.of("cart", cartServiceImpl);
    }

    @PostMapping("/addCart/{productId}")
    public ApiResponse<Cart> addCart(@PathVariable("productId") String productId){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Cart>builder()
                .result(cartService.addProduct(productId))
                .build();
    }

    @PostMapping("/reduceCart/{productId}")
    public ApiResponse<Cart> reduceCart(@PathVariable("productId") String productId){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Cart>builder()
                .result(cartService.reduceProduct(productId))
                .build();
    }

    @DeleteMapping("/deleteCart/{cartItemId}")
    public ApiResponse<Cart> deleteCart(@PathVariable("cartItemId") String cartItemId){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Cart>builder()
                .result(cartService.deleteProduct(cartItemId))
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Integer> getCountMyCart(){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Integer>builder()
                .result(cartService.countMyCart())
                .build();
    }
}
