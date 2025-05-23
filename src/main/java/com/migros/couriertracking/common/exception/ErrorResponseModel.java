package com.migros.couriertracking.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponseModel {

    private final ErrorDetail detail;

    private final String exception;

    private final Instant occurredAt;

}
