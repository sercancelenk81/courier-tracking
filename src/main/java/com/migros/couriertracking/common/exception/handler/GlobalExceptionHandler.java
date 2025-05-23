package com.migros.couriertracking.common.exception.handler;

import com.migros.couriertracking.common.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Locale;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponseModel> handle(Exception exception) {

        log.error("Exception occurred. {}", exception.getStackTrace());

        var message = getMessage(ErrorEnum.UNEXPECTED_ERROR.getMessageKey(), null, "An unexpected error has occurred !!");
        var response = prepareResponseModel(ErrorEnum.UNEXPECTED_ERROR, message, exception);

        return new ResponseEntity<>(response, ErrorEnum.UNEXPECTED_ERROR.getStatus());

    }

    @ExceptionHandler(CourierNotFoundException.class)
    private ResponseEntity<ErrorResponseModel> handle(CourierNotFoundException exception) {

        log.info("CourierNotFoundException occurred. {}", exception);

        var messageKey = exception.getErrorEnum().getMessageKey();
        var httpStatus = exception.getErrorEnum().getStatus();
        final var message = messageSource.getMessage(messageKey, exception.getArgs(), messageKey, Locale.ENGLISH);

        var response = prepareResponseModel(exception.getErrorEnum(), message, exception);
        return new ResponseEntity<>(response, httpStatus);

    }

    @ExceptionHandler(CourierAlreadyExistsException.class)
    private ResponseEntity<ErrorResponseModel> handle(CourierAlreadyExistsException exception) {

        log.info("CourierAlreadyExistsException occurred. {}", exception);

        var messageKey = exception.getErrorEnum().getMessageKey();
        var httpStatus = exception.getErrorEnum().getStatus();
        final var message = messageSource.getMessage(messageKey, exception.getArgs(), messageKey, Locale.ENGLISH);

        var response = prepareResponseModel(exception.getErrorEnum(), message, exception);
        return new ResponseEntity<>(response, httpStatus);

    }

    private String getMessage(String messageKey, String[] args, String defaultMessage) {

        var messageTemplate = messageSource.getMessage(messageKey, null, messageKey, Locale.ENGLISH);

        if (isNull(messageTemplate)) {
            return defaultMessage;
        }

        if (nonNull(args) && args.length > 0) {
            return String.format(messageTemplate, (Object[]) args);
        }

        return defaultMessage;

    }

    private ErrorResponseModel prepareResponseModel(ErrorEnum error, String message, Exception exception) {
        var detail = new ErrorDetail(error.getCode(), message);
        return new ErrorResponseModel(detail, exception.getClass().getSimpleName(), Instant.now());

    }
}
