package com.example.identityService.service.impl;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.User;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.mapper.UserMapper;
import com.example.identityService.repository.*;
import com.example.identityService.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminServiceImpl implements AdminService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    @Override
    public PageResponse<UserResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").ascending());
        var pageData = userRepository.findAll(pageable);
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(userMapper::toUserResponse).toList())
                .build();
    }


    //    @Cacheable(value = "itemCache")
    @Override
    public UserResponse getUserById(String id) {
//        log.info("cache 1231313131");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
        );
    }

    @Override
    @Transactional
    public UserResponse updateUser(String id, UserRequest request) {
        User users = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        users.setUsername(request.getUsername());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(users));
    }

    @Override
    @Transactional
    public String deleteUser(String id) {
        userRepository.deleteById(id);
        return "Delete successfully";
    }

    @Override
    public String deleteProductById(String productId) {
        productRepository.deleteById(productId);
        return "Delete successfully";
    }

    @Override
    public Order updateDelivery(String orderId, boolean delivery) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setDelivery(true);
        return orderRepository.save(order);
    }
}
