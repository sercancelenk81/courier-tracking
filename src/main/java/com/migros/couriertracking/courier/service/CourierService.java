package com.migros.couriertracking.courier.service;

import com.migros.couriertracking.common.exception.CourierAlreadyExistsException;
import com.migros.couriertracking.common.exception.CourierNotFoundException;
import com.migros.couriertracking.common.util.DistanceCalculator;
import com.migros.couriertracking.courier.dto.CourierDistanceResponse;
import com.migros.couriertracking.courier.dto.CourierLocationRequest;
import com.migros.couriertracking.courier.dto.CourierResponse;
import com.migros.couriertracking.courier.dto.CreateOrUpdateCourierRequest;
import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.courier.mapper.CourierMapper;
import com.migros.couriertracking.courier.repository.CourierLocationRepository;
import com.migros.couriertracking.courier.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;

    private final CourierLocationRepository courierLocationRepository;

    private final DistanceCalculator distanceCalculator;

    private final CourierMapper courierMapper;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void createCourier(CreateOrUpdateCourierRequest createCourierRequest) {

        var courierOpt = courierRepository.findByIdentityNumber(createCourierRequest.identityNumber());
        if (courierOpt.isPresent()) {
            throw new CourierAlreadyExistsException(createCourierRequest.identityNumber());
        }

        var courierEntity = courierMapper.toCourierEntity(createCourierRequest);
        courierRepository.save(courierEntity);

        log.info("Courier with identity number {} created successfully", createCourierRequest.identityNumber());
    }

    @Transactional
    public void updateCourier(Long id, CreateOrUpdateCourierRequest updateCourierRequest) {

        var courierEntity = courierRepository.findById(id).orElseThrow(() -> new CourierAlreadyExistsException(updateCourierRequest.identityNumber()));

        var updatedEntity = courierMapper.updateCourierEntity(courierEntity, updateCourierRequest);
        courierRepository.save(updatedEntity);

        log.info("Courier with id {} updated successfully", id);
    }

    @Transactional
    public void updateCourierLocation(CourierLocationRequest courierLocationRequest) {

        Long courierId = courierLocationRequest.courierId();
        var isCourierExist = courierRepository.existsByIdAndDeleted(courierId);

        if (!isCourierExist) {
            throw new CourierNotFoundException(String.valueOf(courierId));
        }

        var courierEntityReference = getEntityReferenceById(courierId);
        var courierLocationEntity = courierMapper.toCourierLocationEntity(courierLocationRequest, courierEntityReference);
        courierLocationRepository.save(courierLocationEntity);

        publisher.publishEvent(courierLocationRequest);
    }

    public CourierDistanceResponse getTotalDistanceByCourierId(Long courierId) {

        var locations = courierLocationRepository.findByCourierIdOrderByTimeAsc(courierId);

        if (locations.isEmpty()) {
            return new CourierDistanceResponse(0.0, 0.0);
        }

        double sum = 0.0;
        for (int i = 1; i < locations.size(); i++) {
            var previousLocation = locations.get(i - 1);
            var currentLocation = locations.get(i);
            sum += distanceCalculator.calculate(previousLocation.getLatitude(), previousLocation.getLongitude(),
                                                currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        return new CourierDistanceResponse(sum, sum*1000);
    }

    public CourierResponse getAllActiveCouriers(Integer page, Integer size) {
        var couriers = courierRepository.findAllByDeletedFalse(PageRequest.of(page,size));
        return new CourierResponse(couriers.getContent(), couriers.getTotalElements());

    }

    public CourierEntity getEntityReferenceById(Long id) {
        return courierRepository.getReferenceById(id);
    }
}
