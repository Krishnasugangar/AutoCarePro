package com.autocarepro.service;

import com.autocarepro.model.Car;
import com.autocarepro.model.Customer;
import com.autocarepro.model.ServiceRequest;
import com.autocarepro.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    public ServiceRequest createRequest(Customer customer, Car car, String serviceType) {
        ServiceRequest request = new ServiceRequest();
        request.setCustomer(customer);
        request.setServiceType(serviceType);
        request.setStatus("Requested");
        request.setRequestDate(LocalDateTime.now());
        return serviceRequestRepository.save(request);
    }

    public List<ServiceRequest> getRequestsByCustomer(Customer customer) {
        return serviceRequestRepository.findByCustomer(customer);
    }

    public List<ServiceRequest> getRequestsByStatus(String status) {
        return serviceRequestRepository.findByStatus(status);
    }

    public ServiceRequest updateStatus(Long requestId, String status, String adminComment) {
        ServiceRequest request = serviceRequestRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setStatus(status);
            if (adminComment != null) {
                request.setAdminComment(adminComment);
            }
            return serviceRequestRepository.save(request);
        }
        return null;
    }
}
