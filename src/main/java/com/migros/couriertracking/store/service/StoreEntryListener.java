package com.migros.couriertracking.store.service;

import com.migros.couriertracking.courier.dto.CourierLocationRequest;
import com.migros.couriertracking.courier.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.migros.couriertracking.common.config.AsyncConfig.VIRTUAL_THREAD_EXECUTOR;
import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreEntryListener {

    private final StoreEntryService storeEntryService;

    private final StoreService storeService;

    private final CourierService courierService;

    @Value("${store.entry.radius}")
    private Double storeEntryRadius;

    @Async(VIRTUAL_THREAD_EXECUTOR)
    @EventListener
    @Transactional
    public void handleStoreEntryEvent(CourierLocationRequest storeEntryEvent) {

        var oneMinuteAgo = storeEntryEvent.time().minusSeconds(60);

        var nearestStore = storeService.findNearestUnvisitedStoreWithinRadiusInLastMinute(storeEntryEvent.lat(),
                                                                                          storeEntryEvent.lng(),
                                                                                          storeEntryRadius,
                                                                                          storeEntryEvent.courierId(),
                                                                                          oneMinuteAgo
        );

        if (isNull(nearestStore)) {
            return;
        }

        var storeReference = storeService.getEntityReferenceById(nearestStore.getId());
        var courierReference = courierService.getEntityReferenceById(storeEntryEvent.courierId());

        storeEntryService.createEntry(courierReference, storeReference, storeEntryEvent.time());
        log.info("Store entry event handled for courier {} and store {}. Distance to store: {}", storeEntryEvent.courierId(), nearestStore.getId(), nearestStore.getDistance());
    }
}
