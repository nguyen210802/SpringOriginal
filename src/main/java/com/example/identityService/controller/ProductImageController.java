package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.response.ProductImageResponse;
import com.example.identityService.entity.ProductImage;
import com.example.identityService.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/product/image")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageController {
    ProductImageService productImageServiceImpl;

    @GetMapping("/getByProduct")
    public ApiResponse<List<ProductImage>> getByProduct(@RequestParam String productId){
        return ApiResponse.<List<ProductImage>>builder()
                .result(productImageServiceImpl.getAllByproductId(productId))
                .build();
    }
}
