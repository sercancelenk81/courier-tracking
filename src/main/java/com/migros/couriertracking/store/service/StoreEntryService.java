package com.migros.couriertracking.store.service;

import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.store.entity.StoreEntity;
import com.migros.couriertracking.store.mapper.StoreEntryMapper;
import com.migros.couriertracking.store.repository.StoreEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StoreEntryService {

    private final StoreEntryRepository storeEntryRepository;

    private final StoreEntryMapper storeEntryMapper;

    @Transactional
    public void createEntry(CourierEntity courier, StoreEntity store, Instant entryTime) {
        storeEntryRepository.save(storeEntryMapper.toStoreEntryEntity(courier, store, entryTime));
    }
}
