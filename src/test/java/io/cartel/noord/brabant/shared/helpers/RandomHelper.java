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

    public static UUID uuid() {
        return randomUUID();
    }

    public static String string() {
        return random(MAX_CHARS, true, true);
    }

    public static int quantity() {
        return new Random().nextInt();
    }

    public static double price() {
        return new Random().nextDouble();
    }

    public static String json() {
        return "{\"id\":%d,\"message\":\"%s\"}".formatted(RandomHelper.quantity(), RandomHelper.string());
    }
}
