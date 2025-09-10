package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.CourierProfileUpdateRequest;
import com.hudfs.hudfs28.dtos.CourierProfileUpdateResponse;
import com.hudfs.hudfs28.dtos.CourierProfileView;
import com.hudfs.hudfs28.dtos.CourierRequest;
import com.hudfs.hudfs28.dtos.CourierResponse;
import com.hudfs.hudfs28.entities.Courier;
import com.hudfs.hudfs28.repositories.CourierRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    public CourierResponse register(CourierRequest request) {
        if (isNullOrEmpty(request.getName()) ||
            isNullOrEmpty(request.getMail()) ||
            isNullOrEmpty(request.getPassword()) ||
            isNullOrEmpty(request.getPasswordVerification())) {
            return new CourierResponse(false, null, null, "All the parts must be filled.");
        }

        if (!request.getPassword().equals(request.getPasswordVerification())) {
            return new CourierResponse(false, null, null, "Password and password verification do not match.");
        }

        if (!request.getMail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new CourierResponse(false, null, null, "Mail format is not correct.");
        }

        if (!request.getPassword().matches("^(?=.*[A-Z])(?=.*\\p{Punct}).{8,}$")) {
            return new CourierResponse(false, null, null, "Password format is not correct.");
        }

        if (courierRepository.existsByMail(request.getMail())) {
            return new CourierResponse(false, null, null, "Mail is already registered.");
        }

        Courier courier = new Courier();
        courier.setName(request.getName());
        courier.setMail(request.getMail());
        courier.setPassword(request.getPassword());

        courierRepository.save(courier);

        String token = JwtUtil.generateToken(courier.getMail());

        return new CourierResponse(true, token, courier.getCourierId(), "Courier registered successfully.");
    }

    public CourierProfileUpdateResponse updateProfile(String token, Integer courierId, CourierProfileUpdateRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Courier courier = courierRepository.findByMail(email);

        if (courier == null || !courier.getCourierId().equals(courierId)) {
            return new CourierProfileUpdateResponse(false, "Unauthorized.");
        }

        if (request.getTelephoneNumber() != null &&
            !request.getTelephoneNumber().matches("^0\\d{3} \\d{3} \\d{2} \\d{2}$")) {
            return new CourierProfileUpdateResponse(false, "Phone number must be in format: 0XXX XXX XX XX.");
        }

        boolean changed = false;

        if (request.getName() != null && !request.getName().equals(courier.getName())) {
            courier.setName(request.getName());
            changed = true;
        }

        if (request.getAge() != null && !request.getAge().equals(courier.getAge())) {
            courier.setAge(request.getAge());
            changed = true;
        }

        if (request.getTelephoneNumber() != null && !request.getTelephoneNumber().equals(courier.getTelephoneNumber())) {
            courier.setTelephoneNumber(request.getTelephoneNumber());
            changed = true;
        }

        if (!changed) {
            return new CourierProfileUpdateResponse(false, "There is no change.");
        }

        courierRepository.save(courier);
        return new CourierProfileUpdateResponse(true, "Profile updated successfully.");
    }

    public CourierProfileUpdateResponse toggleAvailability(String token, Integer courierId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Courier courier = courierRepository.findByMail(email);

        if (courier == null || !courier.getCourierId().equals(courierId)) {
            return new CourierProfileUpdateResponse(false, "Unauthorized.");
        }

        if (!courier.isAvailable()) {
            return new CourierProfileUpdateResponse(false, "You are currently in a delivery. Cannot toggle to available.");
        }

        courier.setAvailable(false); // sadece available → unavailable geçişe izin veriliyor
        courierRepository.save(courier);
        return new CourierProfileUpdateResponse(true, "Courier is now unavailable.");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public CourierProfileView viewProfile(String token, Integer courierId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Courier courier = courierRepository.findByMail(email);

        if (courier == null || !courier.getCourierId().equals(courierId)) {
            return null;
        }

        CourierProfileView view = new CourierProfileView();
        view.setCourierId(courier.getCourierId());
        view.setName(courier.getName());
        view.setMail(courier.getMail());
        view.setAge(courier.getAge());
        view.setTelephoneNumber(courier.getTelephoneNumber());
        view.setLevel(courier.getLevel());
        view.setPoints(courier.getPoints());
        view.setAvailable(courier.isAvailable());

        return view;
    }
}
