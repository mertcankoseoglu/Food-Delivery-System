package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.CustomerTaskRequest;
import com.hudfs.hudfs28.dtos.CustomerTaskResponse;
import com.hudfs.hudfs28.dtos.CustomerTaskView;
import com.hudfs.hudfs28.services.CustomerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
public class CustomerTaskController {
    @Autowired
    private CustomerTaskService customerTaskService;
    
    @PostMapping("/{customerId}/tasks/assign-customer-task")
    public CustomerTaskResponse assignTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long customerId,
            @RequestBody CustomerTaskRequest request) {
        token = token.replace("Bearer ", "");
        return customerTaskService.assignTaskToCustomer(token, customerId, request);
    }


    @GetMapping("/{customerId}/tasks")
    public List<CustomerTaskView> getTasksForCustomer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long customerId) {
        token = token.replace("Bearer ", "");
        return customerTaskService.getTasksForCustomer(token, customerId);
    }

}
