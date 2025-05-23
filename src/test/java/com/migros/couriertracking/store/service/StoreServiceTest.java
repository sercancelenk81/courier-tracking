package com.migros.couriertracking.store.service;

import com.migros.couriertracking.store.dto.StoreProjection;
import com.migros.couriertracking.store.entity.StoreEntity;
import com.migros.couriertracking.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void should_find_nearest_unvisited_store_within_radius_in_last_minute() {

        //GIVEN
        double latitude = 40.7128;
        double longitude = -74.0060;
        double radius = 100.0;
        long courierId = 1L;
        Instant time = Instant.now();
        StoreProjection storeProjection = new StoreProjection() {

            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Ataşehir MMM Migros";
            }

            @Override
            public Double getDistance() {
                return 81.0;
            }
        };

        when(storeRepository.findNearestUnvisitedStoreInLastMinute(courierId, latitude, longitude, radius, time))
                .thenReturn(storeProjection);

        //WHEN
        var response = storeService.findNearestUnvisitedStoreWithinRadiusInLastMinute(latitude, longitude, radius, courierId, time);

        //THEN
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Ataşehir MMM Migros", response.getName());
        assertEquals(81.0, response.getDistance());

    }

    @Test
    void should_return_null_when_no_store_found() {

        //GIVEN
        double latitude = 40.7128;
        double longitude = -74.0060;
        double radius = 100.0;
        long courierId = 1L;
        Instant time = Instant.now();

        when(storeRepository.findNearestUnvisitedStoreInLastMinute(courierId, latitude, longitude, radius, time))
                .thenReturn(null);

        //WHEN
        var response = storeService.findNearestUnvisitedStoreWithinRadiusInLastMinute(latitude, longitude, radius, courierId, time);

        //THEN
        assertNull(response);
    }

    @Test
    void should_return_entity_reference_by_id() {

        //GIVEN
        Long storeId = 1L;
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setId(storeId);

        when(storeRepository.getReferenceById(storeId)).thenReturn(storeEntity);

        //WHEN
        var response = storeService.getEntityReferenceById(storeId);

        //THEN
        assertNotNull(response);
        assertEquals(storeId, response.getId());
    }
}
