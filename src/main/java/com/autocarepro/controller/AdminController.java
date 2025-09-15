package com.autocarepro.controller;

import com.autocarepro.model.ServiceRequest;
import com.autocarepro.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<ServiceRequest> pendingRequests = serviceRequestService.getRequestsByStatus("Requested");
        List<ServiceRequest> completedRequests = serviceRequestService.getRequestsByStatus("Completed");
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("completedRequests", completedRequests);
        return "admin_dashboard"; // Ensure this matches your Thymeleaf file name
    }


    @PostMapping("/update-request")
    public String updateRequestStatus(@RequestParam Long requestId,
                                      @RequestParam String status,
                                      @RequestParam(required = false) String comment) {
        serviceRequestService.updateStatus(requestId, status, comment);
        return "redirect:/admin/dashboard";
    }
}
