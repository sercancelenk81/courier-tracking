package com.migros.couriertracking.courier.controller;

import com.migros.couriertracking.courier.dto.CourierDistanceResponse;
import com.migros.couriertracking.courier.dto.CourierLocationRequest;
import com.migros.couriertracking.courier.dto.CourierResponse;
import com.migros.couriertracking.courier.dto.CreateOrUpdateCourierRequest;
import com.migros.couriertracking.courier.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1/courier")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @GetMapping
    public ResponseEntity<CourierResponse> getCouriers(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.of(Optional.of(courierService.getAllActiveCouriers(page, size)));
    }

    @PostMapping
    public ResponseEntity<Void> createCourier(@RequestBody @Valid CreateOrUpdateCourierRequest createCourierRequest) {
        courierService.createCourier(createCourierRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourier(@PathVariable Long id, @RequestBody @Valid CreateOrUpdateCourierRequest updateCourierRequest) {
        courierService.updateCourier(id, updateCourierRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/location")
    public ResponseEntity<Void> updateCourierLocation(@RequestBody @Valid CourierLocationRequest courierLocationRequest) {
        courierService.updateCourierLocation(courierLocationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/total-distance")
    public ResponseEntity<CourierDistanceResponse> getTotalDistance(@PathVariable Long id) {
        return ResponseEntity.of(Optional.of(courierService.getTotalDistanceByCourierId(id)));
    }
}
