package com.migros.couriertracking.courier.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateCourierRequest(@NotBlank String name,
                                           @NotBlank String surname,
                                           @NotBlank String phoneNumber,
                                           @NotBlank String identityNumber) {
}
