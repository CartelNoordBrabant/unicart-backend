package io.cartel.noord.brabant.api.dtos;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.cartel.noord.brabant.api.enums.DiffResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = ANY)
public record CartResponse(
        @NotNull UUID id,
        @NotNull List<ProviderResponse> providers
) implements Serializable {
}
