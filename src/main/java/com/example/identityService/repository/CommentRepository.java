package com.example.identityService.repository;

import com.example.identityService.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByProduct_Id(String productId);
}
