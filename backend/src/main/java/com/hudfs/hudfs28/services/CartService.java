package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.CartAddRequest;
import com.hudfs.hudfs28.dtos.CartItemResponse;
import com.hudfs.hudfs28.dtos.CartUpdateRequest;
import com.hudfs.hudfs28.entities.*;
import com.hudfs.hudfs28.repositories.*;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private MenuRepository menuRepo;
    @Autowired private CartRepository cartRepo;
    @Autowired private CartItemRepository cartItemRepo;
    @Autowired private CouponRepository couponRepo;

    public Map<String, String> addToCart(Long customerId, CartAddRequest request, String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String tokenEmail = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepo.findByMail(tokenEmail);

        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return response("false", "unauthorized");
        }

        Cart cart = cartRepo.findByCustomer(customer);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cartRepo.save(cart);
        }

        // === Farklı restoran kontrolü ===
        if (!cart.getItems().isEmpty()) {
            Long existingRestaurantId = null;
            CartItem existingItem = cart.getItems().get(0);

            if (existingItem.isProductItem()) {
                existingRestaurantId = existingItem.getProduct().getRestaurant().getId();
            } else if (existingItem.isMenuItem()) {
                existingRestaurantId = existingItem.getMenu().getRestaurant().getId();
            }

            Long newRestaurantId = null;
            if ("product".equalsIgnoreCase(request.getType())) {
                Product product = productRepo.findById(request.getTargetId()).orElse(null);
                if (product == null) return response("false", "item could not be found");
                newRestaurantId = product.getRestaurant().getId();

                if (!existingRestaurantId.equals(newRestaurantId)) {
                    return response("false", "You can only order from one restaurant at a time.");
                }

                if (cartItemRepo.findByCartAndProduct(cart, product).isPresent())
                    return response("false", "item already in the cart");

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(1);
                cartItemRepo.save(item);
                return response("true", "item added to the cart");

            } else if ("menu".equalsIgnoreCase(request.getType())) {
                Menu menu = menuRepo.findById(request.getTargetId()).orElse(null);
                if (menu == null) return response("false", "item could not be found");
                newRestaurantId = menu.getRestaurant().getId();

                if (!existingRestaurantId.equals(newRestaurantId)) {
                    return response("false", "You can only order from one restaurant at a time.");
                }

                if (cartItemRepo.findByCartAndMenu(cart, menu).isPresent())
                    return response("false", "item already in the cart");

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setMenu(menu);
                item.setQuantity(1);
                cartItemRepo.save(item);
                return response("true", "item added to the cart");
            }

            return response("false", "invalid item type");

        } else {
            if ("product".equalsIgnoreCase(request.getType())) {
                Product product = productRepo.findById(request.getTargetId()).orElse(null);
                if (product == null) return response("false", "item could not be found");

                if (cartItemRepo.findByCartAndProduct(cart, product).isPresent())
                    return response("false", "item already in the cart");

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(1);
                cartItemRepo.save(item);
                return response("true", "item added to the cart");

            } else if ("menu".equalsIgnoreCase(request.getType())) {
                Menu menu = menuRepo.findById(request.getTargetId()).orElse(null);
                if (menu == null) return response("false", "item could not be found");

                if (cartItemRepo.findByCartAndMenu(cart, menu).isPresent())
                    return response("false", "item already in the cart");

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setMenu(menu);
                item.setQuantity(1);
                cartItemRepo.save(item);
                return response("true", "item added to the cart");
            }

            return response("false", "invalid item type");
        }
    }

    

    public Map<String, String> updateCartItemQuantity(Long customerId, Long itemId, CartUpdateRequest request, String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String tokenEmail = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepo.findByMail(tokenEmail);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return response("false", "unauthorized");
        }
    
        Optional<CartItem> itemOpt = cartItemRepo.findById(itemId);
        if (itemOpt.isEmpty()) return response("false", "item could not be found");
    
        CartItem item = itemOpt.get();
        if (!item.getCart().getCustomer().getCustomerId().equals(customerId)) {
            return response("false", "unauthorized");
        }
    
        int change = request.getNumber();
        if (change != 1 && change != -1) {
            return response("false", "only 1 or -1 are valid");
        }
    
        int newQty = item.getQuantity() + change;
    
        if (newQty <= 0) {
            cartItemRepo.delete(item);
            return response("true", "item removed from cart");
        }
    
        item.setQuantity(newQty);
        cartItemRepo.save(item);
    
        return response("true", change == 1 ? "item number increased" : "item number decreased");
    }
    

    public Map<String, Object> getCartItems(Long customerId, String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String tokenEmail = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepo.findByMail(tokenEmail);

        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return Map.of("success", false, "detail", "unauthorized");
        }

        Cart cart = cartRepo.findByCustomer(customer);
        List<CartItemResponse> responseList = new ArrayList<>();

        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                if (item.isProductItem()) {
                    Product p = item.getProduct();
                    responseList.add(new CartItemResponse(
                            item.getId(),
                            "product",
                            p.getName(),
                            item.getQuantity(),
                            p.getPrice().floatValue(),
                            p.getRestaurant().getName()
                    ));
                } else if (item.isMenuItem()) {
                    Menu m = item.getMenu();
                    Restaurant r = m.getRestaurant();
                    responseList.add(new CartItemResponse(
                            item.getId(),
                            "menu",
                            m.getName(),
                            item.getQuantity(),
                            m.getPrice().floatValue(),
                            r.getName()
                    ));
                }
            }
        }

        // Kullanıcıya ait geçerli kuponları getir
        List<Coupon> coupons = couponRepo.findByCustomer(customer);
        List<Map<String, Object>> couponViews = new ArrayList<>();

        Date now = new Date();
        for (Coupon c : coupons) {
            if (c.getExpiryDate().after(now)) {
                couponViews.add(Map.of(
                    "code", c.getCode(),
                    "discountAmount", c.getDiscountAmount(),
                    "minimumOrderAmount", c.getMinimumOrderAmount(),
                    "expiryDate", c.getExpiryDate(),
                    "couponId", c.getCouponId()
                ));
            }
        }

        return Map.of(
            "success", true,
            "items", responseList,
            "coupons", couponViews
        );
    }
    
    private Map<String, String> response(String success, String detail) {
        Map<String, String> map = new HashMap<>();
        map.put("success", success);
        map.put("detail", detail);
        return map;
    }
}