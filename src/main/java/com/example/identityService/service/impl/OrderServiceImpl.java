package com.example.identityService.service.impl;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.OrderItemRequest;
import com.example.identityService.entity.*;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.*;
import com.example.identityService.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    AddressRepository addressRepository;
    NotificationRepository notificationRepository;

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
    public Order createOrder(String addressId, Set<OrderItemRequest> requests) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String buyerId = authenticated.getName();

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Address not found"));
        if(!address.getBuyer().getId().equals(buyerId))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Order order = new Order();
        order.setBuyer(userRepository.findById(buyerId).orElseThrow());
        order.setAddress(address);
        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        double totalAmount = 0;

        for(OrderItemRequest request : requests){
            Product product = productRepository.findById(request.getProductId()).orElseThrow();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(request.getProductId())
                    .productPrice(product.getPrice())
                    .linkProduct(request.getLinkProduct())
                    .quantity(request.getQuantity())
                    .price(product.getPrice() * request.getQuantity())
                    .build();

            orderItemRepository.save(orderItem);

            totalAmount += orderItem.getPrice();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setDelivery(false);

        Notification notification = Notification.builder()
                .user(userRepository.findByUsername("admin"))
                .message("Ban co mot don hang moi can xac nhan")
                .read(false)
                .build();
        notificationRepository.save(notification);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
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
