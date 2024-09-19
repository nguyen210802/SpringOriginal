package com.example.identityService.service;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.entity.Comment;

import java.util.List;

public interface CommentService {
    PageResponse<Comment> getAll(int page, int size);
    PageResponse<Comment> getAllByProductId(String productId, int page, int size);
    Comment getByCommentId(String commentId);
    Comment create(String productId, Comment comment);
    Comment update(Comment comment);
    String delete(String id);
}
