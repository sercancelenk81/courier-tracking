package com.migros.couriertracking.store.service;

import com.migros.couriertracking.store.dto.StoreProjection;
import com.migros.couriertracking.store.entity.StoreEntity;
import com.migros.couriertracking.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreProjection findNearestUnvisitedStoreWithinRadiusInLastMinute(Double latitude,
                                                                             Double longitude,
                                                                             Double radius,
                                                                             Long courierId,
                                                                             Instant time) {
        return storeRepository.findNearestUnvisitedStoreInLastMinute(courierId, latitude, longitude, radius, time);
    }

    public StoreEntity getEntityReferenceById(Long id) {
        return storeRepository.getReferenceById(id);
    }
}
