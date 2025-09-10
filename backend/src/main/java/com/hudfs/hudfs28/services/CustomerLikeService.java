package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.LikeRequest;
import com.hudfs.hudfs28.entities.*;
import com.hudfs.hudfs28.repositories.*;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerLikeService {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private MenuRepository menuRepo;
    @Autowired private CustomerLikesProductRepository likesProductRepo;
    @Autowired private CustomerLikesMenuRepository likesMenuRepo;

    public Map<String, Object> handleLike(LikeRequest request, String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String tokenEmail = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepo.findByMail(tokenEmail);

        if (customer == null || !customer.getCustomerId().equals(request.getCustomerId())) {
            return fail("Unauthorized action.");
        }

        String type = request.getType().toLowerCase();
        String action = request.getAction().toLowerCase();

        switch (type) {
            case "product":
                return handleProductReaction(customer, request.getTargetId(), action);
            case "menu":
                return handleMenuReaction(customer, request.getTargetId(), action);
            default:
                return fail("Invalid type.");
        }
    }

    private Map<String, Object> handleProductReaction(Customer customer, Long productId, String action) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return fail("Product not found.");

        CustomerLikesProduct existing = likesProductRepo.findByCustomerAndProduct(customer, product);

        switch (action) {
            case "like":
            case "dislike":
                ReactionType reaction = action.equals("like") ? ReactionType.LIKE : ReactionType.DISLIKE;
                if (existing != null) {
                    if (existing.getReactionType() == reaction) {
                        return fail("Already " + action + "d this product.");
                    }
                    existing.setReactionType(reaction);
                    likesProductRepo.save(existing);
                    return success("Product reaction updated to " + action + ".");
                } else {
                    likesProductRepo.save(new CustomerLikesProduct(customer, product, reaction));
                    return success("Product " + action + "d successfully.");
                }

            case "unlike":
            case "undislike":
                if (existing == null) return fail("No existing reaction to remove.");
                ReactionType expected = action.equals("unlike") ? ReactionType.LIKE : ReactionType.DISLIKE;
                if (existing.getReactionType() != expected) {
                    return fail("You haven't " + expected.name().toLowerCase() + "d this product.");
                }
                likesProductRepo.delete(existing);
                return success("Product " + action + "d successfully.");

            default:
                return fail("Invalid action.");
        }
    }

    private Map<String, Object> handleMenuReaction(Customer customer, Long menuId, String action) {
        Menu menu = menuRepo.findById(menuId).orElse(null);
        if (menu == null) return fail("Menu not found.");

        CustomerLikesMenu existing = likesMenuRepo.findByCustomerAndMenu(customer, menu);

        switch (action) {
            case "like":
            case "dislike":
                ReactionType reaction = action.equals("like") ? ReactionType.LIKE : ReactionType.DISLIKE;
                if (existing != null) {
                    if (existing.getReactionType() == reaction) {
                        return fail("Already " + action + "d this menu.");
                    }
                    existing.setReactionType(reaction);;
                    likesMenuRepo.save(existing);
                    return success("Menu reaction updated to " + action + ".");
                } else {
                    likesMenuRepo.save(new CustomerLikesMenu(customer, menu, reaction));
                    return success("Menu " + action + "d successfully.");
                }

            case "unlike":
            case "undislike":
                if (existing == null) return fail("No existing reaction to remove.");
                ReactionType expected = action.equals("unlike") ? ReactionType.LIKE : ReactionType.DISLIKE;
                if (existing.getReactionType() != expected) {
                    return fail("You haven't " + expected.name().toLowerCase() + "d this menu.");
                }
                likesMenuRepo.delete(existing);
                return success("Menu " + action + "d successfully.");

            default:
                return fail("Invalid action.");
        }
    }

    private Map<String, Object> success(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("detail", msg);
        return map;
    }

    private Map<String, Object> fail(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("detail", msg);
        return map;
    }
}
