package io.cartel.noord.brabant.api.dtos;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record ItemDTO(
        @NotNull String name,
        @NotNull String code,
        @NotNull String imageURL,
        @NotNull BigDecimal price,
        @NotNull Integer amount
) implements Serializable {
}
