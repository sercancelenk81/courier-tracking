package com.migros.couriertracking.store.service;

import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.store.entity.StoreEntity;
import com.migros.couriertracking.store.mapper.StoreEntryMapper;
import com.migros.couriertracking.store.repository.StoreEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreEntryServiceTest {

    @InjectMocks
    private StoreEntryService storeEntryService;

    @Mock
    private StoreEntryRepository storeEntryRepository;

    @Mock
    private StoreEntryMapper storeEntryMapper;

    @Test
    void should_create_entry() {

        //GIVEN
        var courier = new CourierEntity();
        courier.setId(1L);
        var store = new StoreEntity();
        store.setId(1L);
        var entryTime = Instant.now();

        //WHEN
        storeEntryService.createEntry(courier, store, entryTime);

        //THEN
        verify(storeEntryRepository).save(storeEntryMapper.toStoreEntryEntity(courier, store, entryTime));
    }
}
