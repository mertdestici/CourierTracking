package com.example.couriertracking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {
    private String name;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;
}
