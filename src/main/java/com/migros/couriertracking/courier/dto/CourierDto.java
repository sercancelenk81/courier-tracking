package com.migros.couriertracking.courier.dto;

import java.time.LocalDateTime;

public record CourierDto(Long id, String name, String surname, String identityNumber, String phoneNumber, LocalDateTime createdAt) {
}
