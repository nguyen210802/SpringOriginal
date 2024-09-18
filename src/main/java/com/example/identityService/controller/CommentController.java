package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
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

    @GetMapping("/getAllByProduct/{productId}")
    public ApiResponse<PageResponse<Comment>> getAllByProduct(@PathVariable("productId") String productId,
    @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        CommentService commentService = (CommentService) map.get("comment");
        return ApiResponse.<PageResponse<Comment>>builder()
               .result(commentService.getAllByProductId(productId, page, size))
               .build();
    }

    @PostMapping("/create/{productId}")
    public ApiResponse<Comment> create(@PathVariable("productId") String productId, @RequestBody Comment comment){
        CommentService commentService = (CommentService) map.get("comment");
        return ApiResponse.<Comment>builder()
                .result(commentService.create(productId, comment))
                .build();
    }

    @DeleteMapping("/delete/{commentId}")
    public ApiResponse<String> delete(@PathVariable("commentId") String commentId){
        CommentService commentService = (CommentService) map.get("comment");
        return ApiResponse.<String>builder()
               .result(commentService.delete(commentId))
               .build();
    }
}
