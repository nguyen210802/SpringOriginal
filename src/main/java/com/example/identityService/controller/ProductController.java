package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.ProductRequest;
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
    public ApiResponse<PageResponse<Product>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductService productService = (ProductService) map.get("product");

        return ApiResponse.<PageResponse<Product>>builder()
                .result(productService.getAll(page, size))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<Product> getProductById(@PathVariable("productId") String productId){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.getProductById(productId))
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
    public ApiResponse<Product> createProduct(@RequestBody ProductRequest request){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.create(request))
                .build();
    }

    @PutMapping("/update/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable("productId") String productId, @RequestBody Product request){
        log.info("Product update Controller");
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.update(productId, request))
                .build();
    }

    @DeleteMapping("/delete/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable("productId") String id){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<String>builder()
                .result(productService.delete(id))
                .build();
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
}
