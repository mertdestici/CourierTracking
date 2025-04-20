package com.example.couriertracking.service;

import com.example.couriertracking.dto.Store;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.couriertracking.model.*;
import com.example.couriertracking.repository.*;
import com.example.couriertracking.util.DistanceCalculator;
import com.example.couriertracking.util.StoreRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourierLocationServiceTest {
    @Mock
    private CourierRepository courierRepository;
    @Mock
    private CourierLocationRepository locationRepository;
    @Mock
    private CourierStoreEntryRepository entryRepository;
    @Mock
    private DistanceCalculator calculator;

    @InjectMocks
    private CourierLocationService service;


    @Test
    void shouldSaveCourierAndLocation() {
        // Given
        String courierId = "courier_001";
        CourierLocation location = CourierLocation.builder()
                .latitude(41.0)
                .longitude(29.0)
                .timestamp(LocalDateTime.now())
                .build();
        when(courierRepository.findByCourierId(courierId)).thenReturn(Optional.empty());
        when(courierRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        service.logLocation(location, courierId);

        // Then
        ArgumentCaptor<CourierLocation> captor = ArgumentCaptor.forClass(CourierLocation.class);
        verify(locationRepository).save(captor.capture());

        assertThat(captor.getValue().getCourier().getCourierId()).isEqualTo(courierId);
    }

    @Test
    void shouldReturnTotalDistance() {
        // Given
        Courier courier = Courier.builder()
                .id(1L)
                .courierId("courier_001")
                .locations(new ArrayList<>())
                .entries(new ArrayList<>())
                .build();

        List<CourierLocation> locations = List.of(
                CourierLocation.builder()
                        .id(1L)
                        .courier(courier)
                        .latitude(41.0)
                        .longitude(29.0)
                        .timestamp(LocalDateTime.now())
                        .build(),

                CourierLocation.builder()
                        .id(2L)
                        .courier(courier)
                        .latitude(41.001)
                        .longitude(29.002)
                        .timestamp(LocalDateTime.now().plusMinutes(1))
                        .build()
        );

        when(locationRepository.findByCourierCourierIdOrderByTimestampAsc("courier_001")).thenReturn(locations);
        when(calculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(150.0);

        // When
        double distance = service.getTotalDistance("courier_001");

        // Then
        assertThat(distance).isEqualTo(150.0);
    }

    @Test
    void shouldReturnNearbyCouriers() {
        // Given
        Store store = new Store("Migros Beşiktaş", 41.0, 29.0);
        StoreRegistry.getInstance().getStores().add(store);

        Courier courier = Courier.builder()
                .id(1L)
                .courierId("courier_001")
                .locations(new ArrayList<>())
                .entries(new ArrayList<>())
                .build();
        CourierLocation location = CourierLocation.builder()
                .courier(courier)
                .latitude(41.0)
                .longitude(29.0)
                .timestamp(LocalDateTime.now())
                .build();

        when(locationRepository.findAll()).thenReturn(List.of(location));
        when(calculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(90.0);

        // When
        var nearby = service.getNearbyCouriers("Migros Beşiktaş");

        // Then
        assertThat(nearby).hasSize(1);
        assertThat(nearby.get(0).getCourierId()).isEqualTo("courier_001");
    }
}
