package io.cartel.noord.brabant.domain.exceptions;

import org.jetbrains.annotations.NotNull;

public class CustomerMissingException extends ApplicationKnownException {

    public CustomerMissingException(@NotNull String message) {
        super(message);
    }
}
