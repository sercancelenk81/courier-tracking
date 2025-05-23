package com.migros.couriertracking.courier.dto;

import java.util.List;

public record CourierResponse(List<CourierDto> courier, Long totalCount) {
}
