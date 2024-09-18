package com.example.identityService.service.impl;

import com.example.identityService.entity.Cart;
import com.example.identityService.entity.CartItem;
import com.example.identityService.entity.Product;
import com.example.identityService.repository.CartItemRepository;
import com.example.identityService.repository.CartRepository;
import com.example.identityService.repository.ProductRepository;
import com.example.identityService.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;


    @Override
    public Cart getMyCart() {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String userId = authenticated.getName();
        Cart cart = cartRepository.findByBuyer_Id(userId).orElseThrow();

        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        HashSet<CartItem> items = new HashSet<>(cartItems);

        cart.setCartItems(items);

        return cart;
    }

    @Override
    public Cart update(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void delete(String userId) {
        Cart cart = cartRepository.findByBuyer_Id(userId).orElseThrow();
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public Cart addProduct(String productId) {
        Cart cart = getMyCart();
        Product product = productRepository.findById(productId).orElseThrow();

        Optional<CartItem> existingCartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setPrice(cartItem.getQuantity() * product.getPrice());
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .price(product.getPrice())
                    .build();
        }
        cartItem = cartItemRepository.save(cartItem);

        cart.getCartItems().add(cartItem);

        return cartRepository.save(cart);
    }


    @Override
    @Transactional
    public Cart reduceProduct(String productId) {
        Cart cart = getMyCart();
        Product product = productRepository.findById(productId).orElseThrow();

        Optional<CartItem> existingCartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItem.setPrice(cartItem.getQuantity() * product.getPrice());
        }else
            return null;

        if(cartItem.getQuantity() <= 0){
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }else {
            cartItemRepository.save(cartItem);
        }

        return cartRepository.save(cart);
    }
}
