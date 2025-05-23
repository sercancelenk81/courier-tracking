package com.migros.couriertracking.store.service;

import com.migros.couriertracking.courier.dto.CourierLocationRequest;
import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.courier.service.CourierService;
import com.migros.couriertracking.store.dto.StoreProjection;
import com.migros.couriertracking.store.entity.StoreEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreEntryListenerTest {

    @InjectMocks
    private StoreEntryListener storeEntryListener;

    @Mock
    private StoreEntryService storeEntryService;

    @Mock
    private StoreService storeService;

    @Mock
    private CourierService courierService;

    @Value("${store.entry.radius}")
    private Double storeEntryRadius;

    @Test
    void should_save_entry_when_unvisited_store_in_radius() {

        //GIVEN
        var time = Instant.now();
        var oneMinuteAgo = time.minusSeconds(60);
        var storeEntryEvent = new CourierLocationRequest(40.7128, -74.0060, 1L,  time);
        var storeReference = new StoreEntity();
        storeReference.setId(1L);
        var courierReference = new CourierEntity();
        courierReference.setId(1L);
        var storeProjection = new StoreProjection() {

            @Override
            public Long getId() {
                return storeReference.getId();
            }

            @Override
            public String getName() {
                return "Ataşehir MMM Migros";
            }

            @Override
            public Double getDistance() {
                return 50.0;
            }
        };
        when(storeService.findNearestUnvisitedStoreWithinRadiusInLastMinute(storeEntryEvent.lat(),
                                                                            storeEntryEvent.lng(),
                                                                            storeEntryRadius,
                                                                            storeEntryEvent.courierId(),
                                                                            oneMinuteAgo)).thenReturn(storeProjection);
        when(storeService.getEntityReferenceById(storeProjection.getId())).thenReturn(storeReference);
        when(courierService.getEntityReferenceById(storeEntryEvent.courierId())).thenReturn(courierReference);

        //WHEN
        storeEntryListener.handleStoreEntryEvent(storeEntryEvent);

        //THEN
        verify(storeEntryService, times(1)).createEntry(courierReference, storeReference, storeEntryEvent.time());
        verify(storeEntryService).createEntry(courierReference, storeReference, storeEntryEvent.time());
        verify(storeService).findNearestUnvisitedStoreWithinRadiusInLastMinute(storeEntryEvent.lat(),
                                                                               storeEntryEvent.lng(),
                                                                               storeEntryRadius,
                                                                               storeEntryEvent.courierId(),
                                                                               oneMinuteAgo);
        verify(storeService).getEntityReferenceById(storeProjection.getId());
        verify(courierService).getEntityReferenceById(storeEntryEvent.courierId());
    }

    @Test
    void should_do_nothing_when_there_is_no_unvisited_store_in_radius() {

        //GIVEN
        var time = Instant.now();
        var oneMinuteAgo = time.minusSeconds(60);
        var storeEntryEvent = new CourierLocationRequest(40.7128, -74.0060, 1L, time);
        when(storeService.findNearestUnvisitedStoreWithinRadiusInLastMinute(storeEntryEvent.lat(),
                                                                            storeEntryEvent.lng(),
                                                                            storeEntryRadius,
                                                                            storeEntryEvent.courierId(),
                                                                            oneMinuteAgo)).thenReturn(null);

        //WHEN
        storeEntryListener.handleStoreEntryEvent(storeEntryEvent);

        //THEN
        verify(storeEntryService, times(0)).createEntry(null, null, null);
    }
}
