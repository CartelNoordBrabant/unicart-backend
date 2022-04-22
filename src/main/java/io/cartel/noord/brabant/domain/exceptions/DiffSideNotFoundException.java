package io.cartel.noord.brabant.domain.exceptions;

import io.cartel.noord.brabant.domain.enums.Side;
import org.jetbrains.annotations.NotNull;

/**
 * {@link DiffSideNotFoundException} thrown when a diff side is not found.
 */
public class DiffSideNotFoundException extends ApplicationKnownException {

    private final Side side;

    public DiffSideNotFoundException(@NotNull Side side) {
        super(String.format("Diff %s side was not found", side.getId()));
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
