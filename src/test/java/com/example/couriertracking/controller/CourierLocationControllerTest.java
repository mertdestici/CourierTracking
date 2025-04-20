package com.example.couriertracking.controller;

import com.example.couriertracking.dto.CourierLocationRequest;
import com.example.couriertracking.dto.NearbyCourierResponse;
import com.example.couriertracking.repository.CourierLocationRepository;
import com.example.couriertracking.repository.CourierRepository;
import com.example.couriertracking.repository.CourierStoreEntryRepository;
import com.example.couriertracking.service.CourierLocationService;
import com.example.couriertracking.util.DistanceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(CourierLocationController.class)
class CourierLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DistanceCalculator distanceCalculator;

    @MockitoBean
    private CourierRepository courierRepository;

    @MockitoBean
    private CourierLocationRepository courierLocationRepository;

    @MockitoBean
    private CourierStoreEntryRepository courierStoreEntryRepository;

    @MockitoBean
    private CourierLocationService courierLocationService;

    @Test
    void postLocationShouldReturn200Ok() throws Exception {
        CourierLocationRequest request = new CourierLocationRequest(
                "courier_001", 41.01, 29.02, LocalDateTime.now()
        );

        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "courierId": "courier_001",
                      "latitude": 41.01,
                      "longitude": 29.02,
                      "timestamp": "2025-04-20T12:00:00"
                    }
                """))
                .andExpect(status().isOk());
    }

    @Test
    void getDistanceShouldReturnDistanceValue() throws Exception {
        when(courierLocationService.getTotalDistance("courier_001")).thenReturn(1450.5);

        mockMvc.perform(get("/api/locations/distance/courier_001"))
                .andExpect(status().isOk())
                .andExpect(content().string("1450.5"));
    }

    @Test
    void getNearbyCouriersShouldReturnList() throws Exception {
        var response = List.of(new NearbyCourierResponse("courier_123", 41.0, 29.0, LocalDateTime.now()));
        when(courierLocationService.getNearbyCouriers("Migros Kanyon")).thenReturn(response);

        mockMvc.perform(get("/api/locations/nearby/Migros Kanyon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].courierId").value("courier_123"));
    }
}
