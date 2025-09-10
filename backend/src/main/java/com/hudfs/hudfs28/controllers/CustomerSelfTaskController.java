package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.CustomerTaskResponse;
import com.hudfs.hudfs28.dtos.CustomerTaskView;
import com.hudfs.hudfs28.services.CustomerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerSelfTaskController {

    @Autowired
    private CustomerTaskService customerTaskService;

    // POST /api/customer/{customerId}/task/select
    @PostMapping("/{customerId}/task/select")
    public CustomerTaskResponse selectTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long customerId,
            @RequestBody Map<String, Long> request) {

        token = token.replace("Bearer ", "");
        Long customerTaskId = request.get("customerTaskId");
        return customerTaskService.selectCustomerTask(token, customerId, customerTaskId);
    }

    // POST /api/customer/{customerId}/task/unassign
    @PostMapping("/{customerId}/task/unassign")
    public CustomerTaskResponse unassignTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long customerId,
            @RequestBody Map<String, Long> request) {

        token = token.replace("Bearer ", "");
        Long customerTaskId = request.get("customerTaskId");
        return customerTaskService.unassignCustomerTask(token, customerId, customerTaskId);
    }

    // GET /api/customer/{customerId}/list/assignedtasks
    @GetMapping("/{customerId}/list/assignedtasks")
    public List<CustomerTaskView> getAssignedTasks(
            @RequestHeader("Authorization") String token,
            @PathVariable Long customerId) {

        token = token.replace("Bearer ", "");
        return customerTaskService.getAssignedTasksForCustomer(token, customerId);
    }
}
