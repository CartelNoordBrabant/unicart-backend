package io.cartel.noord.brabant.domain.services;

import static io.cartel.noord.brabant.domain.configs.CacheConfig.DIFF_CACHE;
import static io.cartel.noord.brabant.domain.enums.Side.LEFT;
import static io.cartel.noord.brabant.domain.enums.Side.RIGHT;
import static io.cartel.noord.brabant.shared.helpers.Base64Helper.decodeB64;
import static io.cartel.noord.brabant.shared.helpers.JSONHelper.isValidJSON;

import io.cartel.noord.brabant.domain.entities.DiffSide;
import io.cartel.noord.brabant.domain.enums.Side;
import io.cartel.noord.brabant.domain.exceptions.DiffSideNotFoundException;
import io.cartel.noord.brabant.domain.exceptions.InvalidJsonException;
import io.cartel.noord.brabant.domain.repositories.DiffSideRepository;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@link CartService} abstracts all high level logical operations to handle diffs, like
 * when to persist and retrieve data, when to calculate diffs, caching and eviction.
 */
@Service
public class CartService {

    private final DiffSideRepository sideRepository;
    private final DiffCheckService checkService;

    public CartService(DiffSideRepository sideRepository, DiffCheckService checkService) {
        this.sideRepository = sideRepository;
        this.checkService = checkService;
    }

    /**
     * Saves data for left side. It evicts any cache data related to a diff, so it is updatable.
     *
     * @param id   diff id
     * @param data diff left data, should be JSON base64 encoded, throws an error if it is not
     */
    @CacheEvict(value = DIFF_CACHE, key = "#id")
    public void saveLeft(@NotNull UUID id, @NotNull String data) {
        save(LEFT, id, data);
    }

    /**
     * Saves data for right side. It evicts any cache data related to a diff, so it is updatable.
     *
     * @param id   diff id
     * @param data diff right data, should be JSON base64 encoded, throws an error if it is not
     */
    @CacheEvict(value = DIFF_CACHE, key = "#id")
    public void saveRight(@NotNull UUID id, @NotNull String data) {
        save(RIGHT, id, data);
    }

    private void save(Side side, UUID id, String data) {
        var decodedData = decodeB64(data);

        if (!isValidJSON(decodedData)) {
            throw new InvalidJsonException();
        }

        sideRepository.save(new DiffSide(side, id, decodedData));
    }

    /**
     * Retrieves and calculate diff from left and right sides, throws errors if some of them are not present.
     * Results are cached for a while.
     *
     * <p>A Lazy calculation approach was adopted, so diff is only calculated when it is needed. That approach
     * should largely differ if bigger data needs to be handled.
     *
     * @param id diff id
     */
    @Cacheable(value = DIFF_CACHE, key = "#id")
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
