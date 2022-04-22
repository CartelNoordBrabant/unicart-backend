package io.cartel.noord.brabant.api.dtos;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.cartel.noord.brabant.api.enums.ErrorCode;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record ErrorResponse(
    @NotNull ErrorCode code,
    @NotNull String message
) implements Serializable {
}
