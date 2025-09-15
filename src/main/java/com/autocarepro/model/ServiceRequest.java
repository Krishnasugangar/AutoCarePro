package com.autocarepro.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_requests")
@Data
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;   // Add this field and map it to car table

    private String serviceType;

    private String status; // e.g., Requested, Approved, Completed

    private LocalDateTime requestDate;

    private String adminComment;
    private LocalDateTime completionDate;

    public void markCompleted(String comment) {
        setStatus("Completed");
        setCompletionDate(LocalDateTime.now());
        if(comment != null) setAdminComment(comment);
    }

}
