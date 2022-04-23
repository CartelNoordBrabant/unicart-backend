package io.cartel.noord.brabant.domain.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidItemException extends ApplicationKnownException {

    public InvalidItemException(@NotNull String message) {
        super(message);
    }
}
