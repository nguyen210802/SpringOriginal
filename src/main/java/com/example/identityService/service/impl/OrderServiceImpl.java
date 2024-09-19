package com.example.identityService.service.impl;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.OrderItem;
import com.example.identityService.entity.Product;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.OrderItemRepository;
import com.example.identityService.repository.OrderRepository;
import com.example.identityService.repository.ProductRepository;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    ProductRepository productRepository;
    OrderItemRepository orderItemRepository;
    UserRepository userRepository;

    @Override
    @Cacheable(value = "allOrder", key = "#page"+ '-' + "#size")
    public PageResponse<Order> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        var pageData = orderRepository.findAll(pageable);
        return PageResponse.<Order>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    @Cacheable(value = "order", key = "#orderId")
    public Order getOrder(String orderId) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String buyerId = authenticated.getName();

        Order order = orderRepository.findById(orderId).orElseThrow();
        if(order.getBuyer().getId().equals(buyerId))
            return order;
        else
            throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    @Transactional
    public Order createOrder(Set<OrderItem> orderItems) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String buyerId = authenticated.getName();

        Order order = new Order();
        order.setBuyer(userRepository.findById(buyerId).orElseThrow());
        order = orderRepository.save(order);

        Set<OrderItem> orderItems2 = new HashSet<>();

        double totalAmount = 0;

        for(OrderItem item : orderItems){
            Product product = productRepository.findById(item.getProductId()).orElseThrow();

            item.setOrder(order);
            item.setProductPrice(product.getPrice());
            item.setPrice(product.getPrice() * item.getQuantity());
            orderItemRepository.save(item);

            totalAmount += item.getPrice();

            orderItems2.add(item);
        }

        order.setOrderItems(orderItems2);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    @CacheEvict(value = "order", key = "#orderId")
    public String deleteOrder(String orderId) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String buyerId = authenticated.getName();
        log.info("Authenticated: {}", authenticated.getAuthorities());
        Order order = orderRepository.findById(orderId).orElseThrow();
        boolean hasRoleUser = authenticated.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if(hasRoleUser || order.getBuyer().getId().equals(buyerId)){
            orderRepository.deleteById(orderId);
            log.info("co quyen");
        }
        else
            throw new AppException(ErrorCode.UNAUTHORIZED);
        return "Delete successfully";
    }
}
