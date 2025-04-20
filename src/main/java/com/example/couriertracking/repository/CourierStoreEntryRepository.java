package com.example.couriertracking.repository;

import com.example.couriertracking.model.CourierStoreEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierStoreEntryRepository extends JpaRepository<CourierStoreEntry, Long> {
    Optional<CourierStoreEntry> findTopByCourierCourierIdAndStoreNameOrderByTimestampDesc(String courierId, String storeName);
}
