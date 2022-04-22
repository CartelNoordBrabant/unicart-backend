package io.cartel.noord.brabant.shared.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

/**
 * {@link JSONHelper} static JSON helper methods
 */
public class JSONHelper {

    //JsonMapper is thread-safe so it can be initialized statically
    private static final JsonMapper jsonMapper = new JsonMapper();

    /**
     * Checks if a string is in valid JSON format
     *
     * @param data string to be checked
     * @return (boolean) valid or not
     */
    public static boolean isValidJSON(@NotNull String data) {
        try {
            jsonMapper.readTree(data);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
