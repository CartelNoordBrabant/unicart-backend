package io.cartel.noord.brabant.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Code that represents a known error")
public enum ErrorCode {
    /**
     * Diff side was not posted encoded in base64
     */
    BASE64_INVALID,
    /**
     * Diff side was not posted in JSON format
     */
    JSON_INVALID,
    /**
     * Left diff side was not found
     */
    LEFT_NOT_FOUND,
    /**
     * Right diff side was not found
     */
    RIGHT_NOT_FOUND
}
