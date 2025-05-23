package com.migros.couriertracking.courier.service;

import com.migros.couriertracking.common.exception.CourierAlreadyExistsException;
import com.migros.couriertracking.common.exception.CourierNotFoundException;
import com.migros.couriertracking.common.util.DistanceCalculator;
import com.migros.couriertracking.courier.dto.CourierLocationRequest;
import com.migros.couriertracking.courier.dto.CreateOrUpdateCourierRequest;
import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.courier.entity.CourierLocationEntity;
import com.migros.couriertracking.courier.mapper.CourierMapper;
import com.migros.couriertracking.courier.repository.CourierLocationRepository;
import com.migros.couriertracking.courier.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @InjectMocks
    private CourierService courierService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private DistanceCalculator distanceCalculator;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    void should_create_courier() {

        //GIVEN
        var createCourierRequest = new CreateOrUpdateCourierRequest("Sercan", "Çelenk", "1234567890", "11223344556");
        var courierEntity = CourierEntity.builder().name(createCourierRequest.name())
                                         .surname(createCourierRequest.surname())
                                         .phoneNumber(createCourierRequest.phoneNumber())
                                         .identityNumber(createCourierRequest.identityNumber())
                                         .build();
        when(courierRepository.findByIdentityNumber(createCourierRequest.identityNumber())).thenReturn(Optional.empty());
        when(courierMapper.toCourierEntity(createCourierRequest)).thenReturn(courierEntity);

        //WHEN
        courierService.createCourier(createCourierRequest);

        //THEN
        verify(courierRepository).save(courierEntity);
    }

    @Test
    void should_throw_when_courier_already_exist() {

        //GIVEN
        var createCourierRequest = new CreateOrUpdateCourierRequest("Sercan", "Çelenk", "1234567890", "11223344556");
        when(courierRepository.findByIdentityNumber(createCourierRequest.identityNumber())).thenReturn(Optional.of(new CourierEntity()));

        //WHEN & THEN
        assertThrows(CourierAlreadyExistsException.class, () -> {
            courierService.createCourier(createCourierRequest);
        });
    }

    @Test
    void should_save_courier_location() {

        //GIVEN
        var courierLocationRequest = new CourierLocationRequest(40.7128, 74.0060, 1L, Instant.now());

        var courierReference = new CourierEntity();
        courierReference.setId(courierLocationRequest.courierId());

        var courierLocationEntity = new CourierLocationEntity();
        courierLocationEntity.setCourier(courierReference);
        courierLocationEntity.setLatitude(courierLocationRequest.lat());
        courierLocationEntity.setLongitude(courierLocationRequest.lng());
        courierLocationEntity.setTime(courierLocationRequest.time());

        when(courierRepository.existsByIdAndDeleted(courierLocationRequest.courierId())).thenReturn(true);
        when(courierRepository.getReferenceById(courierLocationRequest.courierId())).thenReturn(courierReference);
        when(courierMapper.toCourierLocationEntity(courierLocationRequest, courierReference)).thenReturn(courierLocationEntity);

        //WHEN
        courierService.updateCourierLocation(courierLocationRequest);

        //THEN
        verify(courierLocationRepository, times(1)).save(courierLocationEntity);
        verify(publisher, times(1)).publishEvent(courierLocationRequest);
        verify(courierMapper, times(1)).toCourierLocationEntity(courierLocationRequest, courierReference);
    }

    @Test
    void should_throw_when_courier_not_found_for_location_update() {

        //GIVEN
        var courierLocationRequest = new CourierLocationRequest(40.7128, 74.0060, 1L, Instant.now());
        when(courierRepository.existsByIdAndDeleted(courierLocationRequest.courierId())).thenReturn(false);

        //WHEN & THEN
        assertThrows(CourierNotFoundException.class, () -> {
            courierService.updateCourierLocation(courierLocationRequest);
        });
    }

    @Test
    void should_calculate_total_distance_by_courier_id() {

        //GIVEN
        Long courierId = 1L;
        var courierEntity = new CourierEntity();
        courierEntity.setId(courierId);

        var locations = List.of(
            new CourierLocationEntity(Instant.now(), 40.7128, -74.0060, courierEntity),
            new CourierLocationEntity(Instant.now().plusSeconds(60), 40.7138, -74.0070, courierEntity)
        );

        when(courierLocationRepository.findByCourierIdOrderByTimeAsc(courierId)).thenReturn(locations);
        when(distanceCalculator.calculate(40.7128, -74.0060, 40.7138, -74.0070)).thenReturn(100.0);

        //WHEN
        var response = courierService.getTotalDistanceByCourierId(courierId);

        //THEN
        assertEquals(100.0, response.distanceInKm());
        assertEquals(100000.0, response.distanceInMeter());
    }
}
