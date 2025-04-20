package com.example.couriertracking.repository;

import com.example.couriertracking.model.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    List<CourierLocation> findByCourierCourierIdOrderByTimestampAsc(String courierId);
}

