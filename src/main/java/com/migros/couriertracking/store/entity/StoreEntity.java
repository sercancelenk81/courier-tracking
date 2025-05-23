package com.migros.couriertracking.store.entity;

import com.migros.couriertracking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store", indexes = {@Index(name = "idx_store_name", columnList = "name")})
public class StoreEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point location;
}
