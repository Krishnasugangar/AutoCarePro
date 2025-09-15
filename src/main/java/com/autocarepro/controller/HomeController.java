package com.autocarepro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.autocarepro.model.Customer;
import com.autocarepro.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String landingPage(Model model) {
        // Services to display in footer
        List<String> services = List.of("Oil Change", "Tire Rotation", "Brake Inspection", "Engine Diagnostics");
        model.addAttribute("services", services);
        return "landing";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(Customer customer, String confirmPassword, Model model) {
        // Basic validation checks
        if (!customer.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "register";
        }
        if (customerRepository.existsByUsername(customer.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists");
            return "register";
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            model.addAttribute("errorMessage", "Email already registered");
            return "register";
        }

        // Encode password before saving
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return "redirect:/login?registerSuccess";
    }

}
