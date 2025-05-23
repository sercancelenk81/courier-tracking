package com.migros.couriertracking.store.entity;

import com.migros.couriertracking.common.entity.BaseEntity;
import com.migros.couriertracking.courier.entity.CourierEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store_entry", indexes = {@Index(name = "idx_store_entry_courier_id_created_at", columnList = "courier_id, created_at")})
public class StoreEntryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private CourierEntity courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Column(name = "entry_time", nullable = false)
    private Instant entryTime;
}
