package com.autocarepro.controller;

import com.autocarepro.model.Car;
import com.autocarepro.model.Customer;
import com.autocarepro.model.ServiceRequest;
import com.autocarepro.repository.CarRepository;
import com.autocarepro.repository.CustomerRepository;
import com.autocarepro.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRequestService serviceRequestService;


    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String customerDashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByUsername(username).orElse(null);

        if (customer != null) {
            List<ServiceRequest> requests = serviceRequestService.getRequestsByCustomer(customer);
            List<Car> cars = carRepository.findByCustomer(customer); // We'll create Car & repo next
            model.addAttribute("username", customer.getName().toUpperCase()); // Full name uppercase
            model.addAttribute("requests", requests);
            model.addAttribute("cars", cars);
        }
        return "customer_dashboard";
    }

    @PostMapping("/add-car")
    public String addCar(@RequestParam String carType, @RequestParam String model,
                         @RequestParam String registrationNumber) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        if (customer != null) {
            Car car = new Car();
            car.setCarType(carType);
            car.setModel(model);
            car.setRegistrationNumber(registrationNumber);
            car.setCustomer(customer);
            carRepository.save(car);
        }
        return "redirect:/customer/dashboard";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        if (customer != null) {
            if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
                model.addAttribute("passwordError", "Old password is incorrect");
                return customerDashboard(model);
            }
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            model.addAttribute("passwordSuccess", "Password changed successfully");
        }
        return customerDashboard(model);
    }


    @PostMapping("/request-service")
    public String requestService(@RequestParam("serviceType") String serviceType,
                                 @RequestParam("carId") Long carId, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByUsername(username).orElse(null);

        if (customer != null) {
            Car car = carRepository.findById(carId).orElse(null);
            if (car != null) {
                serviceRequestService.createRequest(customer, car, serviceType);
            }
        }
        return "redirect:/customer/dashboard";
    }

}
