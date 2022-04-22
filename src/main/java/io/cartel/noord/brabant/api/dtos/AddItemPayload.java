package io.cartel.noord.brabant.api.dtos;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record AddItemPayload(
        @NotNull String provider,
        @NotNull ItemPayload item
) implements Serializable {
}
