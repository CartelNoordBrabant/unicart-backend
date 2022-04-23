package io.cartel.noord.brabant.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Code that represents a known error")
public enum ErrorCode {
    CUSTOMER_MISSING,
    INVALID_ITEM,
    CART_NOT_FOUND
}
