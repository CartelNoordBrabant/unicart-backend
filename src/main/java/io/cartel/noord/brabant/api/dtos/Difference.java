package io.cartel.noord.brabant.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Schema(description = "A single difference found between 2 strings. Holds data that refers its position")
@JsonAutoDetect(fieldVisibility = ANY)
public record Difference(
        @Schema(description = "Offset at which this difference starts. First offset is 0(Zero)")
        @NotNull Long offset,
        @Schema(description = "Total length (in chars) of this difference")
        @NotNull Long length
) implements Serializable {
}
