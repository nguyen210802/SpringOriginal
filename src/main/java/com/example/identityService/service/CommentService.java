package com.example.identityService.service;

import com.example.identityService.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAll();
    List<Comment> getAllByProductId(String productId);
    Comment create(String productId, Comment comment);
    Comment update(Comment comment);
    String delete(String id);
}
