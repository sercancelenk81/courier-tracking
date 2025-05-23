package com.migros.couriertracking.common.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CourierNotFoundException extends RuntimeException implements Supplier<CourierNotFoundException> {

    private final ErrorEnum errorEnum;

    private final String[] args;

    public CourierNotFoundException(String... args) {
        this.errorEnum = ErrorEnum.COURIER_NOT_FOUND;
        this.args = args;
    }

    @Override
    public CourierNotFoundException get() {
        return this;
    }
}
