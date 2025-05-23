package com.migros.couriertracking.store.mapper;

import com.migros.couriertracking.store.dto.StoreJsonDto;
import com.migros.couriertracking.store.entity.StoreEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StoreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", expression = "java(toPoint(storeJsonDto.lat(), storeJsonDto.lng()))")
    StoreEntity toStoreEntityFromJsonDto(StoreJsonDto storeJsonDto);

    default Point toPoint(Double lat, Double lng) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(lng, lat));
    }
}
