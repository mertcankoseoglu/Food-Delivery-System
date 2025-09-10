package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.ExploreMenuDTO;
import com.hudfs.hudfs28.services.CustomerExploreMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/explore/menus")
public class CustomerExploreMenuController {

    @Autowired private CustomerExploreMenuService exploreMenuService;

    @GetMapping
    public ResponseEntity<List<ExploreMenuDTO>> getExploreMenus(@RequestHeader("Authorization") String authHeader) {
        List<ExploreMenuDTO> menus = exploreMenuService.getExploreMenus(authHeader);
        return ResponseEntity.ok(menus);
    }
}
