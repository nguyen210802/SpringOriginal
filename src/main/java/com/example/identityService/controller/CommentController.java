package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.entity.Comment;
import com.example.identityService.service.CommentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/product/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentController {
    Map<String, Object> map;

    public CommentController(CommentService commentServiceImpl) {
        this.map = Map.of("comment", commentServiceImpl);
    }

    @GetMapping("/getAllByProduct")
    public ApiResponse<List<Comment>> getAllByUser(@RequestParam String productId){
        CommentService commentService = (CommentService) map.get("comment");
        return ApiResponse.<List<Comment>>builder()
               .result(commentService.getAllByProductId(productId))
               .build();
    }

    @PostMapping("/create")
    public ApiResponse<Comment> create(@RequestParam String productId, @RequestBody Comment comment){
        CommentService commentService = (CommentService) map.get("comment");
        return ApiResponse.<Comment>builder()
                .result(commentService.create(productId, comment))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> delete(@RequestParam String commentId){
        CommentService commentService = (CommentService) map.get("comment");
        return ApiResponse.<String>builder()
               .result(commentService.delete(commentId))
               .build();
    }
}
