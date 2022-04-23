package io.cartel.noord.brabant.domain.exceptions;

import org.jetbrains.annotations.NotNull;

public class CartNotFoundException extends ApplicationKnownException {

    public CartNotFoundException(@NotNull String message) {
        super(message);
    }
}
