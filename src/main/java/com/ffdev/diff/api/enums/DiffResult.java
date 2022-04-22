package com.ffdev.diff.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Code that represents a diff result")
public enum DiffResult {
    /**
     * diff sides are the same size but different in content
     */
    DIFFERENT,
    /**
     * diff sides have different sizes
     */
    DIFFERENT_SIZES,
    /**
     * diff sides are equal
     */
    EQUAL
}
