package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.AvatarRequest;
import com.hudfs.hudfs28.services.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AvatarController {

    @Autowired private AvatarService avatarService;

    @PostMapping("/admin/avatar/create")
    public ResponseEntity<Map<String, String>> createAvatar(
            @RequestBody AvatarRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(avatarService.createAvatar(request, token));
    }

    @DeleteMapping("/admin/avatar/delete/{avatarId}")
    public ResponseEntity<Map<String, String>> deleteAvatar(
            @PathVariable Long avatarId,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(avatarService.deleteAvatar(avatarId, token));
    }

    @GetMapping("/admin/avatar/list")
    public ResponseEntity<List<Map<String, Object>>> listAvatars(
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(avatarService.listAvatars(token));
    }

    @PostMapping("/customer/{customerId}/avatar/select")
    public ResponseEntity<Map<String, String>> selectAvatar(
            @PathVariable Long customerId,
            @RequestBody AvatarRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(avatarService.selectAvatar(customerId, request, token));
    }

    @PutMapping("/customer/{customerId}/avatar/update")
    public ResponseEntity<Map<String, String>> updateAvatar(
            @PathVariable Long customerId,
            @RequestBody AvatarRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(avatarService.updateAvatar(customerId, request, token));
    }

    @GetMapping("/customer/{customerId}/view/avatar")
    public ResponseEntity<Map<String, Object>> getCustomerAvatar(
            @PathVariable Long customerId,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(avatarService.getCustomerAvatar(customerId, token));
    }

}
