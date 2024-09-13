package com.example.identityService.service;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.OrderItem;
import org.hibernate.mapping.Map;

import java.util.List;
import java.util.Set;

public interface OrderService {
    PageResponse<Order> getAll(int page, int size);
    Order getOrder(String orderId);
    Order createOrder(Set<OrderItem> orderItems);
    String deleteOrder(String orderId);
}
