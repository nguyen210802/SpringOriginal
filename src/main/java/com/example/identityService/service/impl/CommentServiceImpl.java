package com.example.identityService.service.impl;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Comment;
import com.example.identityService.entity.Product;
import com.example.identityService.entity.User;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.CommentRepository;
import com.example.identityService.repository.ProductRepository;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    ProductRepository productRepository;
    UserRepository userRepository;

    @Override
    @Cacheable(value = "allComments", key = "'allComments'")
    public PageResponse<Comment> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").descending());
        var pageData = commentRepository.findAll(pageable);

        return PageResponse.<Comment>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public PageResponse<Comment> getAllByProductId(String productId, int page, int size) {
        productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").descending());
        var pageData = commentRepository.findAllByProduct_Id(productId, pageable);

        return PageResponse.<Comment>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public Comment getByCommentId(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    @Transactional
    public Comment create(String productId, Comment comment) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("product not exited"));

        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(authenticated.getName()).orElseThrow();
        comment.setBuyer(user);

        comment.setProduct(product);

        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public String delete(String id) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(authenticated.getName()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Comment not found"));
        if(comment.getBuyer().getId().equals(user.getId())){
            commentRepository.deleteById(id);
            return "Delete successfully";
        }else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
