package com.example.couriertracking.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DistanceCalculator {
    private final DistanceStrategy strategy;

    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        return strategy.calculate(lat1, lon1, lat2, lon2);
    }
}
