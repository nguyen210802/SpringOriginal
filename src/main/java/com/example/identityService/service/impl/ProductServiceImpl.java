package com.example.identityService.service.impl;

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
    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product create(Product product) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();

        product.setUser(userRepository.findById(authenticated.getName()).orElseThrow());
        return productRepository.save(product);
    }

    @Override
    public Product update(String id, Product update) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();

        Product product = productRepository.findById(id).orElseThrow(
                () ->new IllegalStateException("Product not exited"));

        if(authenticated.getName().equals(product.getUser().getId())){
            product.setName(update.getName());
            product.setImage(update.getImage());
            product.setDescribe(update.getDescribe());
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

        if(authenticated.getName().equals(product.getUser().getId())){
            productRepository.deleteById(id);
        }
        else{
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return "delete successfully";
    }
}
