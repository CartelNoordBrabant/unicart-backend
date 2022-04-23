package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.api.dtos.ErrorDTO;
import io.cartel.noord.brabant.domain.exceptions.CartNotFoundException;
import io.cartel.noord.brabant.domain.exceptions.CustomerMissingException;
import io.cartel.noord.brabant.domain.exceptions.InvalidItemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static io.cartel.noord.brabant.api.enums.ErrorCode.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleCartNotFoundException(CartNotFoundException ex) {
        return new ErrorDTO(CART_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CustomerMissingException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorDTO handleCustomerMissingException(CustomerMissingException ex) {
        return new ErrorDTO(CUSTOMER_MISSING, ex.getMessage());
    }

    @ExceptionHandler(InvalidItemException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorDTO handleInvalidItemException(InvalidItemException ex) {
        return new ErrorDTO(INVALID_ITEM, ex.getMessage());
    }
}
