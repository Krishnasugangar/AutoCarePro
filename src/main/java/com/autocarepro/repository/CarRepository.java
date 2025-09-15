package com.autocarepro.repository;

import com.autocarepro.model.Car;
import com.autocarepro.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByCustomer(Customer customer);
}
