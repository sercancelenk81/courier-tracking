package com.migros.couriertracking.courier.repository;

import com.migros.couriertracking.courier.entity.CourierLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierLocationRepository extends JpaRepository<CourierLocationEntity, Long> {

    List<CourierLocationEntity> findByCourierIdOrderByTimeAsc(Long courierId);
}
