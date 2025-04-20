package com.example.couriertracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearbyCourierResponse {
    private String courierId;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
}
