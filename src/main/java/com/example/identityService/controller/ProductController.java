package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Cart;
import com.example.identityService.entity.Product;
import com.example.identityService.service.CartService;
import com.example.identityService.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    Map<String, Object> map;

    public ProductController(ProductService productServiceImpl, CartService cartServiceImpl) {
        this.map = Map.of("product", productServiceImpl, "cart", cartServiceImpl);
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<Product>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductService productService = (ProductService) map.get("product");

        return ApiResponse.<PageResponse<Product>>builder()
                .result(productService.getAll(page, size))
                .build();
    }

    @GetMapping("")
    public ApiResponse<Product> getProductById(@RequestParam String id){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @GetMapping("/myProduct")
    public ApiResponse<PageResponse<Product>> getMyProduct(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<PageResponse<Product>>builder()
                .result(productService.getAllMyProduct(page, size))
                .build();
    }


    @PostMapping("/create")
    public ApiResponse<Product> createProduct(@RequestBody Product product){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.create(product))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<Product> updateProduct(@RequestParam String id, @RequestBody Product product){
        log.info("Product update Controller");
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.update(id, product))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteProduct(@RequestParam String id){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<String>builder()
                .result(productService.delete(id))
                .build();
    }

    @PostMapping("/addCart")
    public ApiResponse<Cart> addCart(@RequestParam String productId){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Cart>builder()
               .result(cartService.addProduct(productId))
               .build();
    }

    @PostMapping("/reduceCart")
    public ApiResponse<Cart> reduceCart(@RequestParam String productId){
        CartService cartService = (CartService) map.get("cart");
        return ApiResponse.<Cart>builder()
               .result(cartService.reduceProduct(productId))
               .build();
    }
}
