package com.migros.couriertracking.store.mapper;

import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.store.entity.StoreEntity;
import com.migros.couriertracking.store.entity.StoreEntryEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.time.Instant;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StoreEntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    StoreEntryEntity toStoreEntryEntity(CourierEntity courier, StoreEntity store, Instant entryTime);
}
