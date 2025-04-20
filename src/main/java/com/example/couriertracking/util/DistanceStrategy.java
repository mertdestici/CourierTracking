package com.example.couriertracking.util;

public interface DistanceStrategy {
    double calculate(double lat1, double lon1, double lat2, double lon2);
}
