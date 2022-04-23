package io.cartel.noord.brabant.domain.repositories;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cartel.noord.brabant.domain.entities.Cart;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.entities.Provider;
import io.cartel.noord.brabant.domain.exceptions.CartNotFoundException;
import io.cartel.noord.brabant.domain.exceptions.CustomerMissingException;
import io.cartel.noord.brabant.domain.exceptions.InvalidItemException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
            throw new InvalidItemException(
                "Cart (%s) Provider (%s) Item could not be serialized. Error %s".formatted(
                    id, providerId, e.getMessage())
            );
        }
    }

    public void removeItem(
        @NotNull UUID id,
        @NotNull String providerId,
        @NotNull String code
    ) {
        redisHash.delete(providerKey(id, providerId), code);
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

    public Cart getCart(@NotNull UUID id) {
        var providerIds = redisSet.members(cartKey(id));
        if (providerIds == null || providerIds.isEmpty()) {
            throw new CartNotFoundException("Cart (%s) was not found".formatted(id));
        }

        var providers = providerIds.stream()
            .map(providerId -> {
                var providerCart = redisHash.entries(providerKey(id, providerId));

                if (providerCart == null || providerCart.isEmpty()) {
                    return null;
                }

                var customerId = readCustomer(providerCart);
                if (customerId == null) {
                    throw new CustomerMissingException(
                        "Cart (%s) Provider (%s)".formatted(id, providerId));
                }

                var items = providerCart.entrySet()
                    .stream()
                    .filter(entry -> !CUSTOMER_FIELD.equals(entry.getKey()))
                    .map(entry -> {
                        try {
                            return mapper.readValue(entry.getValue(), Item.class);
                        } catch (JsonProcessingException e) {
                            throw new InvalidItemException(
                                "Cart (%s) Provider (%s) Item (%s) could not be deserialized. Error %s".formatted(
                                    id, providerId, entry.getValue(), e.getMessage())
                            );
                        }
                    })
                    .collect(toList());

                return new Provider(providerId, customerId, items);
            })
            .filter(Objects::nonNull)
            .collect(toList());

        return new Cart(id, providers);
    }

    private UUID readCustomer(Map<String, String> providerCart) {
        var customerId = providerCart.get(CUSTOMER_FIELD);
        return customerId != null ? UUID.fromString(customerId) : null;
    }

    private String cartKey(UUID id) {
        return String.format("cart:%s", id);
    }

    private String providerKey(UUID id, String providerId) {
        return String.format("cart:%s:provider:%s", id, providerId);
    }
}
