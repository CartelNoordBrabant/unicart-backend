package io.cartel.noord.brabant.domain.exceptions;

/**
 * {@link InvalidBase64Exception} thrown when base64 string cannot be decoded.
 */
public class InvalidBase64Exception extends ApplicationKnownException {
    public InvalidBase64Exception() {
        super("Invalid base 64 data");
    }
}
