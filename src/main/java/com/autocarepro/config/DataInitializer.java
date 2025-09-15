package com.autocarepro.config;

import com.autocarepro.model.Admin;
import com.autocarepro.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminPassword = "admin123"; // You can change this
        if (adminRepository.findByUsername(adminUsername).isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            adminRepository.save(admin);
            System.out.println("Admin user initialized with username: " + adminUsername + " and password: " + adminPassword);
        }
    }
}
