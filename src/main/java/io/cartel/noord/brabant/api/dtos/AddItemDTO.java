package io.cartel.noord.brabant.api.dtos;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record AddItemDTO(
    @NotNull UUID customerId,
    @NotNull ItemDTO item
) implements Serializable {
}
