package io.cartel.noord.brabant.shared.helpers;

import java.util.Random;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.random;

/**
 * {@link RandomHelper} static random helper methods.
 */
public class RandomHelper {

    //So we do not generate strings too big
    private static final int MAX_CHARS = 40;

    /**
     * Provides a random UUID string.
     *
     * @return random uuid string
     */
    public static UUID uuid() {
        return randomUUID();
    }

    /**
     * Provides a random alphanumeric string.
     *
     * @return random string
     */
    public static String string() {
        return random(MAX_CHARS, true, true);
    }

    /**
     * Provides a random long number.
     *
     * @return random long
     */
    public static long number() {
        return new Random().nextLong();
    }

    /**
     * Provides a kind of random JSON string.
     *
     * @return random JSON string
     */
    public static String json() {
        return "{\"id\":%d,\"message\":\"%s\"}".formatted(RandomHelper.number(), RandomHelper.string());
    }
}
