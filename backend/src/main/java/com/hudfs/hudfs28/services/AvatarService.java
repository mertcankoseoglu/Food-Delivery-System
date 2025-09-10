package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.AvatarRequest;
import com.hudfs.hudfs28.entities.Avatar;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.repositories.AvatarRepository;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AvatarService {

    @Autowired private AvatarRepository avatarRepo;
    @Autowired private CustomerRepository customerRepo;

    // Admin: create avatar
    public Map<String, String> createAvatar(AvatarRequest request, String token) {
        if (!isAdmin(token)) return fail("anauthorized");

        avatarRepo.save(new Avatar(request.getName()));
        return success("avatar is created");
    }

    // Admin: delete avatar
    public Map<String, String> deleteAvatar(Long avatarId, String token) {
        if (!isAdmin(token)) return fail("anauthorized");

        Optional<Avatar> avatarOpt = avatarRepo.findById(avatarId);
        if (avatarOpt.isEmpty()) {
            return fail("avatar cannot be found");
        }

        avatarRepo.deleteById(avatarId);
        return success("avatar is deleted");
            }

    // Admin: list avatars
    public List<Map<String, Object>> listAvatars(String token) {
        if (!isAdmin(token)) return List.of();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Avatar avatar : avatarRepo.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", avatar.getId());
            map.put("name", avatar.getName());
            result.add(map);
        }
        return result;
    }

    // Customer: select avatar
    public Map<String, String> selectAvatar(Long customerId, AvatarRequest request, String token) {
        Customer customer = getAuthorizedCustomer(customerId, token);
        if (customer == null) return fail("anauthorized");

        Optional<Avatar> avatarOpt = avatarRepo.findById(request.getAvatarId());
        if (avatarOpt.isEmpty()) return fail("avatar cannot be found");

        customer.setAvatar(avatarOpt.get());
        customerRepo.save(customer);
        return success("avatar is selected");
    }

    // Customer: update avatar
    public Map<String, String> updateAvatar(Long customerId, AvatarRequest request, String token) {
        Customer customer = getAuthorizedCustomer(customerId, token);
        if (customer == null) return fail("anauthorized");

        Optional<Avatar> avatarOpt = avatarRepo.findById(request.getAvatarId());
        if (avatarOpt.isEmpty()) return fail("avatar cannot be found");

        customer.setAvatar(avatarOpt.get());
        customerRepo.save(customer);
        return success("avatar is updated");
    }

    public Map<String, Object> getCustomerAvatar(Long customerId, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepo.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return Map.of(
                "success", false,
                "detail", "unauthorized"
            );
        }
    
        Avatar avatar = customer.getAvatar();
        Long avatarId = (avatar != null) ? avatar.getId() : null;

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("avatarId", avatarId);
        return result;
    }       

    private Customer getAuthorizedCustomer(Long customerId, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepo.findByMail(email);
        return (customer != null && customer.getCustomerId().equals(customerId)) ? customer : null;
    }

    private boolean isAdmin(String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        return email != null && email.contains("admin"); // adjust for real role check
    }

    private Map<String, String> success(String msg) {
        Map<String, String> res = new HashMap<>();
        res.put("success", "true");
        res.put("detail", msg);
        return res;
    }

    private Map<String, String> fail(String msg) {
        Map<String, String> res = new HashMap<>();
        res.put("success", "false");
        res.put("detail", msg);
        return res;
    }
}
