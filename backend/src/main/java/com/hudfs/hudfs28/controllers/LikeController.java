package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.LikeRequest;
import com.hudfs.hudfs28.services.CustomerLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class LikeController {

    @Autowired
    private CustomerLikeService customerLikeService;

    @PostMapping("/like")
    public ResponseEntity<?> likeOrDislike(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody LikeRequest request) {
        return ResponseEntity.ok(customerLikeService.handleLike(request, authHeader));
    }
}
