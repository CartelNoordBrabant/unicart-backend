package io.cartel.noord.brabant.domain.entities;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record Cart(
        @NotNull UUID id,
        @NotNull List<Provider> providers
) implements Serializable {
}
