package com.autocarepro.repository;

import com.autocarepro.model.ServiceRequest;
import com.autocarepro.model.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    @EntityGraph(attributePaths = {"car", "customer"})
    List<ServiceRequest> findByCustomer(Customer customer);

    @EntityGraph(attributePaths = {"car"})
    List<ServiceRequest> findByStatus(String status);
}
