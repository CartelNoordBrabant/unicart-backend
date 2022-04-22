package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ErrorResponse;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;
import com.ffdev.diff.domain.exceptions.InvalidJsonException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.ffdev.diff.api.enums.ErrorCode.*;
import static com.ffdev.diff.domain.enums.Side.LEFT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Provides a JSON {@link com.ffdev.diff.api.dtos.ErrorResponse ErrorResponse}
 * for known application errors extending {@link ControllerAdvice @ControllerAdvice}
 * for exception handling.
 *
 * <p>Only known exceptions are handled at the moment, as their behaviour is expected and
 * explicitly thrown.
 *
 * <p>Other exception types, or even a base {@link java.lang.Exception Exception}
 * could be added if found necessary. It is not currently done so default Spring behaviour
 * is not overridden so dealing with logging is not necessary.
 */
@ControllerAdvice
public class DiffExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DiffSideNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleDiffSideNotFoundException(DiffSideNotFoundException ex) {
        var errorCode = ex.getSide().equals(LEFT)
                ? LEFT_NOT_FOUND
                : RIGHT_NOT_FOUND;

        return new ErrorResponse(errorCode, ex.getMessage());
    }

    @ExceptionHandler(InvalidBase64Exception.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidBase64Exception(InvalidBase64Exception ex) {
        return new ErrorResponse(BASE64_INVALID, ex.getMessage());
    }

    @ExceptionHandler(InvalidJsonException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidJsonException(InvalidJsonException ex) {
        return new ErrorResponse(JSON_INVALID, ex.getMessage());
    }
}
