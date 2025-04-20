package com.example.couriertracking.service;

import com.example.couriertracking.dto.NearbyCourierResponse;
import com.example.couriertracking.dto.Store;
import com.example.couriertracking.model.Courier;
import com.example.couriertracking.model.CourierLocation;
import com.example.couriertracking.model.CourierStoreEntry;
import com.example.couriertracking.repository.CourierLocationRepository;
import com.example.couriertracking.repository.CourierRepository;
import com.example.couriertracking.repository.CourierStoreEntryRepository;
import com.example.couriertracking.util.DistanceCalculator;
import com.example.couriertracking.util.StoreRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class CourierLocationService {
    private final CourierRepository courierRepo;
    private final CourierLocationRepository locationRepository;
    private final CourierStoreEntryRepository entryRepository;
    private final DistanceCalculator distanceCalculator;

    public CourierLocationService(CourierRepository courierRepo, CourierLocationRepository locationRepository, CourierStoreEntryRepository entryRepository, DistanceCalculator distanceCalculator) {
        this.courierRepo = courierRepo;
        this.locationRepository = locationRepository;
        this.entryRepository = entryRepository;
        this.distanceCalculator = distanceCalculator;
    }

    public void logLocation(CourierLocation location, String courierId) {
        Courier courier = courierRepo.findByCourierId(courierId)
                .orElseGet(() -> courierRepo.save(Courier.builder()
                                                         .courierId(courierId)
                                                         .locations(new ArrayList<>())
                                                         .entries(new ArrayList<>())
                                                         .build()));
        location.setCourier(courier);
        locationRepository.save(location);
        checkStoreProximity(location);
    }

    private void checkStoreProximity(CourierLocation location) {
        var stores = StoreRegistry.getInstance().getStores();
        var courierId = location.getCourier().getCourierId();

        stores.stream()
                .filter(store -> distanceCalculator.calculate(location.getLatitude(), location.getLongitude(), store.getLatitude(), store.getLongitude()) <= 100)
                .forEach(store -> {
                    var lastEntry = entryRepository.findTopByCourierCourierIdAndStoreNameOrderByTimestampDesc(courierId, store.getName());
                    var shouldLog = lastEntry.map(e -> Duration.between(e.getTimestamp(), location.getTimestamp()).toMinutes() > 1)
                            .orElse(true);
                    if (shouldLog) {
                        var entry = CourierStoreEntry.builder()
                                .courier(location.getCourier())
                                .storeName(store.getName())
                                .timestamp(location.getTimestamp())
                                .build();
                        entryRepository.save(entry);
                    }
                });
    }

    public double getTotalDistance(String courierId) {
        var locations = locationRepository.findByCourierCourierIdOrderByTimestampAsc(courierId);
        if (locations.size() < 2) return 0;

        return IntStream.range(1, locations.size())
                .mapToDouble(i -> {
                    var p1 = locations.get(i - 1);
                    var p2 = locations.get(i);
                    return distanceCalculator.calculate(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
                })
                .sum();
    }

    public List<NearbyCourierResponse> getNearbyCouriers(String storeName) {
        Optional<Store> storeOpt = StoreRegistry.getInstance().getStores().stream()
                .filter(store -> store.getName().equalsIgnoreCase(storeName))
                .findFirst();

        if (storeOpt.isEmpty()) {
            return List.of();
        }
        Store store = storeOpt.get();

        return locationRepository.findAll().stream()
                .filter(loc -> distanceCalculator.calculate(loc.getLatitude(), loc.getLongitude(), store.getLatitude(), store.getLongitude()) <= 100)
                .map(loc -> new NearbyCourierResponse(
                        loc.getCourier().getCourierId(),
                        loc.getLatitude(),
                        loc.getLongitude(),
                        loc.getTimestamp()))
                .collect(Collectors.toList());
    }
}
