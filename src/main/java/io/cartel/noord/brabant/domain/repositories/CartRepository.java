package io.cartel.noord.brabant.domain.repositories;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cartel.noord.brabant.domain.entities.Cart;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.entities.Provider;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartRepository {

    private static final String CUSTOMER_FIELD = "customer";

    private final StringRedisTemplate redis;
    private final HashOperations<String, String, String> redisHash;
    private final SetOperations<String, String> redisSet;
    private final ObjectMapper mapper;

    public CartRepository(StringRedisTemplate redisTemplate, ObjectMapper mapper) {
        this.mapper = mapper;
        this.redis = redisTemplate;
        this.redisHash = redisTemplate.opsForHash();
        this.redisSet = redisTemplate.opsForSet();
    }

    public void addItem(
        @NotNull UUID id,
        @NotNull String providerId,
        @NotNull UUID customerId,
        @NotNull Item item
    ) {
        try {

            var itemJson = mapper.writeValueAsString(item);

            redisSet.add(cartKey(id), providerId);

            redisHash.putAll(
                providerKey(id, providerId),
                Map.of(
                    CUSTOMER_FIELD, customerId.toString(),
                    item.code(), itemJson
                )
            );

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(
        @NotNull UUID id,
        @NotNull String providerId,
        @NotNull String code
    ) {
        redisHash.delete(providerKey(id, providerId), code);
    }

    public Cart getCart(@NotNull UUID id) {
        var providerIds = redisSet.members(cartKey(id));
        if (providerIds == null) {
            return null;
        }

        var providers = providerIds.stream()
            .map(providerId -> {
                var providerCart = redisHash.entries(providerKey(id, providerId));

                if (providerCart == null || providerCart.isEmpty()) {
                    return null;
                }

                var customerId = Optional.ofNullable(providerCart.get(CUSTOMER_FIELD))
                    .map(UUID::fromString)
                    .orElse(null);

                if (customerId == null) {
                    return null;
                }

                var items = providerCart.entrySet()
                    .stream()
                    .map(entry -> {
                        if (CUSTOMER_FIELD.equals(entry.getKey())) {
                            return null;
                        }

                        try {
                            return mapper.readValue(entry.getValue(), Item.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList());

                return new Provider(providerId, customerId, items);
            })
            .filter(Objects::nonNull)
            .collect(toList());

        return new Cart(id, providers);
    }

    private String cartKey(UUID id) {
        return String.format("cart:%s", id);
    }

    private String providerKey(UUID id, String providerId) {
        return String.format("cart:%s:provider:%s", id, providerId);
    }

    public void removeCart(UUID id) {
        var cartKey = cartKey(id);
        var toDelete = new HashSet<String>();
        toDelete.add(cartKey);

        var providersId = redisSet.members(cartKey);
        if (providersId != null) {
            providersId.forEach(providerId -> toDelete.add(providerKey(id, providerId)));
        }

        redis.delete(toDelete);
    }
}
