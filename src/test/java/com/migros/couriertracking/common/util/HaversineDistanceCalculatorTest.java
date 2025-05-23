package com.migros.couriertracking.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HaversineDistanceCalculatorTest {

    private final HaversineDistanceCalculator distanceCalculator = new HaversineDistanceCalculator();

    @Test
    void should_return_zero_when_coordinates_are_equal() {

        //GIVEN
        double lat = 40.7128;
        double lng = -74.0060;

        //WHEN
        double distance = distanceCalculator.calculate(lat, lng, lat, lng);

        //THEN
        assertEquals(0.0, distance);
    }

    @Test
    void should_calculate_when_coordinates_are_different() {

        //GIVEN
        double lat1 = 40.7128;
        double lng1 = -74.0060;
        double lat2 = 34.0522;
        double lng2 = -118.2437;

        //WHEN
        double distance = distanceCalculator.calculate(lat1, lng1, lat2, lng2);

        //THEN
        assertEquals(3936.0, distance, 1);
    }
}
