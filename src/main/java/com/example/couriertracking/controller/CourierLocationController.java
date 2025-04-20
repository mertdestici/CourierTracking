package com.example.couriertracking.controller;

import com.example.couriertracking.dto.CourierLocationRequest;
import com.example.couriertracking.dto.NearbyCourierResponse;
import com.example.couriertracking.model.CourierLocation;
import com.example.couriertracking.service.CourierLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/locations")
public class CourierLocationController {
    private final CourierLocationService service;

    @PostMapping
    public ResponseEntity<Void> postLocation(@Valid @RequestBody CourierLocationRequest request) {
        var location = new CourierLocation();
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setTimestamp(request.getTimestamp());

        service.logLocation(location, request.getCourierId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/distance/{courierId}")
    public ResponseEntity<Double> getDistance(@PathVariable String courierId) {
        return ResponseEntity.ok(service.getTotalDistance(courierId));
    }

    @GetMapping("/nearby/{storeName}")
    public ResponseEntity<List<NearbyCourierResponse>> getNearbyCouriers(@PathVariable String storeName) {
        return ResponseEntity.ok(service.getNearbyCouriers(storeName));
    }
}
