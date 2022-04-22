package io.cartel.noord.brabant.api.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.cartel.noord.brabant.api.enums.DiffResult;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Schema(description = "Calculated diff response. Holds data that refers to its result and differences found")
@JsonAutoDetect(fieldVisibility = ANY)
public record DiffResponse(
        @NotNull DiffResult result,
        @NotNull List<Difference> differences
) implements Serializable {
}
