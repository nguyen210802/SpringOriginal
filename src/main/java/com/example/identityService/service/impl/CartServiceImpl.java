package com.example.identityService.service.impl;

import com.example.identityService.entity.Cart;
import com.example.identityService.entity.User;
import com.example.identityService.repository.CartRepository;
import com.example.identityService.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;


    @Override
    public Cart getById(String id) {
        Cart cart = cartRepository.findById(id).orElseThrow();
        return cart;
    }

    @Override
    public Cart getByUserId(String userId) {
        return cartRepository.findByUser_Id(userId).orElseThrow();
    }

    @Override
    @Transactional
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart update(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void delete(String userId) {
        Cart cart = cartRepository.findByUser_Id(userId).orElseThrow();
        cartRepository.delete(cart);
    }
}
