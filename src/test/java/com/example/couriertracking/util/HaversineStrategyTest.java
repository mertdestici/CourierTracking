package com.example.couriertracking.util;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HaversineStrategyTest {

    @InjectMocks
    private HaversineStrategy strategy;

    @Test
    void shouldReturnZeroWhenSameCoordinates() {
        double lat = 41.0;
        double lon = 29.0;

        double distance = strategy.calculate(lat, lon, lat, lon);

        assertThat(distance).isZero();
    }

    @Test
    void shouldCalculateCorrectDistanceBetweenTwoPoints() {
        double lat1 = 41.0438;
        double lon1 = 29.0094;
        double lat2 = 41.0662;
        double lon2 = 29.0169;

        double distance = strategy.calculate(lat1, lon1, lat2, lon2);

        assertThat(distance).isEqualTo(2568.9295444597824);
    }
}
