package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.MenuInfo;
import com.hudfs.hudfs28.dtos.MenuRequest;
import com.hudfs.hudfs28.dtos.MenuUpdateRequest;
import com.hudfs.hudfs28.dtos.ProductResponse;
import com.hudfs.hudfs28.dtos.MenuResponse;
import com.hudfs.hudfs28.services.MenuService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/{restaurantId}/create")
    public MenuResponse createMenu(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @RequestBody MenuRequest request) {
        token = token.replace("Bearer ", "");
        return menuService.createMenu(token, restaurantId, request);
    }

    @GetMapping("/{restaurantId}/menus")
    public List<MenuInfo> getMenu(@RequestHeader("Authorization") String authHeader,
            @PathVariable Long restaurantId) {
        return menuService.listMenus(authHeader, restaurantId);
    }

    @PutMapping("/{restaurantId}/update/{menuId}")
    public ProductResponse updateMenu(@RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequest request) {
        token = token.replace("Bearer ", "");
        return menuService.updateMenu(token, restaurantId, menuId, request);
    }

    @DeleteMapping("/{restaurantId}/delete/{menuId}")
    public MenuResponse deleteMenu(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId) {
        token = token.replace("Bearer ", "");
        return menuService.deleteMenu(token, restaurantId, menuId);
    }
}
