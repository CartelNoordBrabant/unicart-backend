package io.cartel.noord.brabant.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public record PaymentDTO(
        @NotNull String name,
        @NotNull String code,
        @NotNull String imageURL,
        @NotNull BigDecimal price,
        @NotNull Integer amount
) implements Serializable {
}
