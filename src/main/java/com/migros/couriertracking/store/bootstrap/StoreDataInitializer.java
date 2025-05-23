package com.migros.couriertracking.store.bootstrap;

import com.migros.couriertracking.common.util.ObjectMapperCreator;
import com.migros.couriertracking.store.dto.StoreJsonDto;
import com.migros.couriertracking.store.mapper.StoreMapper;
import com.migros.couriertracking.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreDataInitializer implements ApplicationRunner {

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/data/stores.json");
        StoreJsonDto[] stores = ObjectMapperCreator.getInstance().readValue(inputStream, StoreJsonDto[].class);

        for (StoreJsonDto store : stores) {
            boolean exists = storeRepository.existsByName(store.name());
            if (!exists) {
                storeRepository.save(storeMapper.toStoreEntityFromJsonDto(store));
            }
        }

        log.info("Store data initialized from JSON ({} stores)", stores.length);
    }
}
