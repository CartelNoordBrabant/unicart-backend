package io.cartel.noord.brabant.domain.exceptions;


/**
 * {@link ApplicationKnownException} base class to be extended by application specific
 * exceptions.
 *
 * <p> Extends {@link RuntimeException} to keep methods signature clean.
 */
public class ApplicationKnownException extends RuntimeException {

    public ApplicationKnownException(String message) {
        super(message);
    }
}
