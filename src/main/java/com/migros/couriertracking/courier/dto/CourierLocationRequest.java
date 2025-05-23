package com.migros.couriertracking.courier.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record CourierLocationRequest(@NotNull Double lat,
                                     @NotNull Double lng,
                                     @NotNull @Positive Long courierId,
                                     @NotNull @PastOrPresent Instant time) {
}
