package io.cartel.noord.brabant.domain.entities;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.cartel.noord.brabant.api.dtos.ItemDTO;
import java.io.Serializable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record Provider(
    @NotNull String provider,
    @NotNull List<Item> items
) implements Serializable {
}
