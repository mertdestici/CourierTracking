package com.example.couriertracking.repository;

import com.example.couriertracking.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    Optional<Courier> findByCourierId(String courierId);
}
