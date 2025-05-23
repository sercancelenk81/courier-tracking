package com.migros.couriertracking.store.repository;

import com.migros.couriertracking.store.dto.StoreProjection;
import com.migros.couriertracking.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    @Query(value = """ 
            SELECT CASE
                     WHEN ( Count(s) > 0 ) THEN true
                     ELSE false
                   END
            FROM StoreEntity s
            WHERE s.name = :name
            """)
    boolean existsByName(@Param("name") String name);

    @Query(value = """
                SELECT s.id as id,
                       s.name as name,
                       ST_Distance(s.location::geography, ST_MakePoint(:lon, :lat)::geography) as distance
                FROM store s
                WHERE ST_DWithin(s.location::geography, ST_MakePoint(:lon, :lat)::geography, :radius)
                  AND NOT EXISTS (
                      SELECT 1 FROM store_entry e
                      WHERE e.store_id = s.id
                        AND e.courier_id = :courierId
                        AND e.entry_time >= :timeLimit
                  )
                ORDER BY distance
                LIMIT 1
            """, nativeQuery = true)
    StoreProjection findNearestUnvisitedStoreInLastMinute(@Param("courierId") Long courierId,
                                                          @Param("lat") Double lat,
                                                          @Param("lon") Double lon,
                                                          @Param("radius") Double radius,
                                                          @Param("timeLimit") Instant timeLimit);
}
