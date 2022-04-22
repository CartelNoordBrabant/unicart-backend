package io.cartel.noord.brabant.domain.services;

import static io.cartel.noord.brabant.domain.enums.Side.LEFT;
import static io.cartel.noord.brabant.domain.enums.Side.RIGHT;
import static io.cartel.noord.brabant.shared.helpers.Base64Helper.decodeB64;
import static io.cartel.noord.brabant.shared.helpers.JSONHelper.isValidJSON;

import io.cartel.noord.brabant.domain.entities.DiffSide;
import io.cartel.noord.brabant.domain.enums.Side;
import io.cartel.noord.brabant.domain.exceptions.DiffSideNotFoundException;
import io.cartel.noord.brabant.domain.exceptions.InvalidJsonException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    public void addItem(@NotNull UUID id, @NotNull AddItemPayload payload) {
        var decodedData = decodeB64(data);

        if (!isValidJSON(decodedData)) {
            throw new InvalidJsonException();
        }

        sideRepository.save(new DiffSide(side, id, decodedData));
    }

    public DiffResponse getById(@NotNull UUID id) {
        var left = getSideData(LEFT, id);
        var right = getSideData(RIGHT, id);

        return checkService.getDiff(left, right);
    }

    private String getSideData(Side side, UUID id) {
        return sideRepository.fetchDataBySideAndDiffId(side, id)
                .orElseThrow(() -> new DiffSideNotFoundException(side));
    }
}
