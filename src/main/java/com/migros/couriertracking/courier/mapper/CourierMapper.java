package com.migros.couriertracking.courier.mapper;

import com.migros.couriertracking.courier.dto.CourierLocationRequest;
import com.migros.couriertracking.courier.dto.CreateOrUpdateCourierRequest;
import com.migros.couriertracking.courier.entity.CourierEntity;
import com.migros.couriertracking.courier.entity.CourierLocationEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CourierMapper {

    CourierEntity toCourierEntity(CreateOrUpdateCourierRequest createCourierRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identityNumber", ignore = true)
    CourierEntity updateCourierEntity(@MappingTarget CourierEntity courierEntity, CreateOrUpdateCourierRequest createCourierRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courier", source = "courierEntityReference")
    @Mapping(target = "latitude", source = "courierLocationRequest.lat")
    @Mapping(target = "longitude", source = "courierLocationRequest.lng")
    @Mapping(target = "time", source = "courierLocationRequest.time")
    CourierLocationEntity toCourierLocationEntity(CourierLocationRequest courierLocationRequest, CourierEntity courierEntityReference);
}
