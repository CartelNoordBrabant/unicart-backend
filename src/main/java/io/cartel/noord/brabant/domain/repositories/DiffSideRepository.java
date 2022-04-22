package io.cartel.noord.brabant.domain.repositories;

import io.cartel.noord.brabant.domain.entities.DiffSide;
import io.cartel.noord.brabant.domain.enums.Side;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * {@link DiffSideRepository} abstracts operations persist and retrieve diff side data.
 *
 * <p>Redis is used as a datasource because it is fast, reliable and scalable. Since diff use case
 * is of transient data (we do not need to persist data for much time), Redis makes up a good choice.
 *
 * <p> {@link org.springframework.data.repository.CrudRepository CrudRepository} could be used instead.
 * But it comes with many features that we do not need at this moment, like its "save" and "find" methods
 * deals with Redis SETs to make up for partial indexes, which is too much trouble for our simple and
 * straightforward case. Leveraging a simple {@link StringRedisTemplate} is enough for the moment.
 */
@Repository
public class DiffSideRepository {

    private final StringRedisTemplate redisTemplate;
    private final long timeToLive;

    public DiffSideRepository(
            StringRedisTemplate redisTemplate,
            @Value("${diff.cache.ttl-minutes}") long timeToLive
    ) {
        this.redisTemplate = redisTemplate;
        this.timeToLive = timeToLive;
    }

    /**
     * Persist a diff side data for later comparison. This data is stored for a limited amount of time.
     *
     * <p>Redis keys holds up to 500MB of data, which is much more than springs default POST JSON data size,
     * so we should be ok.
     *
     * @param diffSide diff side data to store
     */
    public void save(@NotNull DiffSide diffSide) {
        var key = getKey(diffSide.diffId(), diffSide.side());
        redisTemplate.opsForValue().set(key, diffSide.data(), timeToLive, TimeUnit.MINUTES);
    }

    /**
     * Fetch data for a specific side and id. An empty Optional should be returned if not found.
     *
     * @param side   which side
     * @param diffId which id
     */
    public Optional<String> fetchDataBySideAndDiffId(@NotNull Side side, @NotNull UUID diffId) {
        var key = getKey(diffId, side);
        var data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    private String getKey(UUID id, Side side) {
        // key format example: diff:some-uuid:left
        return String.format("diff:%s:%s", id, side.getId());
    }
}
