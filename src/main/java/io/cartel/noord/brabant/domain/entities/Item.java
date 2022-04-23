package io.cartel.noord.brabant.domain.entities;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@JsonAutoDetect(fieldVisibility = ANY)
public record Item(
    @NotNull String name,
    @NotNull String code,
    @NotNull String imageURL,
    @NotNull BigDecimal price,
    @NotNull Integer amount
) implements Serializable {
}