package com.migros.couriertracking.courier.repository;

import com.migros.couriertracking.courier.dto.CourierDto;
import com.migros.couriertracking.courier.entity.CourierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

    Optional<CourierEntity> findByIdentityNumber(String identityNumber);

    @Query(value = """
            SELECT CASE WHEN (COUNT(c) > 0) THEN TRUE ELSE FALSE END
            FROM CourierEntity c
            WHERE c.id = :id AND c.deleted = FALSE
        """)
    boolean existsByIdAndDeleted(Long id);

    @Query(value = """
            SELECT new com.migros.couriertracking.courier.dto.CourierDto(c.id, c.name, c.surname, c.identityNumber, c.phoneNumber, c.createdAt)
            FROM CourierEntity c
            WHERE c.deleted = FALSE
        """)
    Page<CourierDto> findAllByDeletedFalse(Pageable pageable);
}
