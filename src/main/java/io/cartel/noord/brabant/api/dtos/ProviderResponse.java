package io.cartel.noord.brabant.api.dtos;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record ProviderResponse(
    @NotNull String provider,
    @NotNull List<ItemResponse> items
) implements Serializable {
}
