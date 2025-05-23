package com.migros.couriertracking.store.repository;

import com.migros.couriertracking.store.entity.StoreEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreEntryRepository extends JpaRepository<StoreEntryEntity, Long> {
}
