package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.entity.Product;
import com.example.identityService.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    Map<String, Object> map;

    public ProductController(ProductService productServiceImpl) {
        this.map = Map.of("product", productServiceImpl);
    }

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
