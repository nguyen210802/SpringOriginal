package com.example.identityService.service.impl;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Product;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.ProductRepository;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements ProductService {
    UserRepository userRepository;
    ProductRepository productRepository;

    @Override
    public PageResponse<Product> getAll(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = productRepository.findAll(pageable);
        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public PageResponse<Product> getAllMyProduct(int page, int size) {
        Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
        String sellerId = authenticated.getName();

//        Pageable pageable = PageRequest.of(page - 1, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").descending());
        var pageData = productRepository.findAllBySeller_Id(sellerId, pageable);

        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    @Transactional
    public Product create(Product product) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();

        product.setSeller(userRepository.findById(authenticated.getName()).orElseThrow());
        return productRepository.save(product);
    }

    @Override
    public Product update(String id, Product update) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();

        Product product = productRepository.findById(id).orElseThrow(
                () ->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if(authenticated.getName().equals(product.getSeller().getId())){
            product.setName(update.getName());
            product.setImage(update.getImage());
            product.setDescription(update.getDescription());
            product.setPrice(update.getPrice());
            return productRepository.save(product);
        }
        else{
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public String delete(String id) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();

        Product product = productRepository.findById(id).orElseThrow(
                () ->new IllegalStateException("Product not exited"));

        if(authenticated.getName().equals(product.getSeller().getId())){
            productRepository.deleteById(id);
        }
        else{
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return "delete successfully";
    }
}
