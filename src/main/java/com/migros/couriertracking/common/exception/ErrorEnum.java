package com.migros.couriertracking.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {

    VALIDATION_ERROR("2001", "validation.error", HttpStatus.BAD_REQUEST),

    UNEXPECTED_ERROR("5000", "unexpected.error", HttpStatus.INTERNAL_SERVER_ERROR),

    COURIER_NOT_FOUND("5001", "courier.not.found", HttpStatus.NOT_FOUND),

    COURIER_ALREADY_EXIST("5002", "courier.already.exist", HttpStatus.BAD_REQUEST);

    private final String code;

    private final String messageKey;

    private final HttpStatus status;


}
