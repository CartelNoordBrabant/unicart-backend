package io.cartel.noord.brabant.domain.services;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cartel.noord.brabant.domain.entities.Cart;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.entities.Provider;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper;

    public CartService(StringRedisTemplate redisTemplate, ObjectMapper mapper) {
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    public void addItem(
        @NotNull UUID id,
        @NotNull String provider,
        @NotNull Item item
    ) {
        try {

            var itemJson = mapper.writeValueAsString(item);

            redisTemplate.opsForSet()
                .add(cartKey(id), provider);

            redisTemplate.opsForHash()
                .put(providerKey(id, provider), item.code(), itemJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(
        @NotNull UUID id,
        @NotNull String provider,
        @NotNull String code
    ) {
        redisTemplate.opsForHash()
            .delete(providerKey(id, provider), code);
    }

    public Cart getCart(@NotNull UUID id) {
        var providerIds = redisTemplate.opsForSet().members(cartKey(id));
        if (providerIds == null) {
            return null;
        }

        var providers = providerIds.stream()
            .map(provider -> {
                var items = redisTemplate.opsForHash()
                    .values(providerKey(id, provider))
                    .stream()
                    .map(item -> {
                        try {
                            return mapper.readValue(item.toString(), Item.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList());
                return new Provider(provider, items);
            })
            .collect(toList());

        return new Cart(id, providers);
    }

    private String cartKey(UUID id) {
        return String.format("cart:%s", id);
    }

    private String providerKey(UUID id, String provider) {
        return String.format("cart:%s:provider:%s", id, provider);
    }
}
