package com.example.couriertracking.util;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DistanceCalculatorTest {

    @Mock
    private DistanceStrategy strategy;
    @InjectMocks
    private DistanceCalculator calculator;
    
    @Test
    void shouldDelegateCalculationToStrategy() {
        // Given
        double lat1 = 41.0, lon1 = 29.0, lat2 = 41.01, lon2 = 29.01;
        when(strategy.calculate(lat1, lon1, lat2, lon2)).thenReturn(1234.56);

        // When
        double result = calculator.calculate(lat1, lon1, lat2, lon2);

        // Then
        verify(strategy).calculate(lat1, lon1, lat2, lon2);
        assertThat(result).isEqualTo(1234.56);
    }
}
