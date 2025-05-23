package com.migros.couriertracking.common.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CourierAlreadyExistsException extends RuntimeException implements Supplier<CourierAlreadyExistsException> {

    private final ErrorEnum errorEnum;

    private final String[] args;

    public CourierAlreadyExistsException(String... args) {
        this.errorEnum = ErrorEnum.COURIER_ALREADY_EXIST;
        this.args = args;
    }

    @Override
    public CourierAlreadyExistsException get() {
        return this;
    }}
