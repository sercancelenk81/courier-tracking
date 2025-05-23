package com.migros.couriertracking.courier.entity;

import com.migros.couriertracking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courier_location")
public class CourierLocationEntity extends BaseEntity {

    @Column(name = "time", nullable = false)
    private Instant time;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @JoinColumn(name = "courier_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CourierEntity courier;

}
