package com.example.couriertracking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationRequest {
    @NotNull
    private String courierId;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotNull
    private LocalDateTime timestamp;
}
