package io.cartel.noord.brabant.domain.repositories;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cartel.noord.brabant.api.dtos.CartResponse;
import io.cartel.noord.brabant.api.dtos.ItemPayload;
import io.cartel.noord.brabant.api.dtos.ProviderResponse;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper;

    public CartRepository(StringRedisTemplate redisTemplate, ObjectMapper mapper) {
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    public void addItem(
        @NotNull UUID id,
        @NotNull String provider,
        @NotNull ItemPayload payload
    ) {
        try {
            redisTemplate.opsForSet().add(cartKey(id), provider);
            redisTemplate.opsForHash()
                .put(providerKey(id, provider), payload.code(), mapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(UUID id, String provider, String code) {
        redisTemplate.opsForHash()
            .delete(providerKey(id, provider), code);
    }

    public CartResponse getCart(UUID id) {
        var providers = redisTemplate.opsForSet().members(cartKey(id))
            .stream()
            .map(provider -> {
                var items = redisTemplate.opsForHash()
                    .values(providerKey(id, provider))
                    .stream()
                    .map(item -> {
                        try {
                            return mapper.readValue(item.toString(), ItemPayload.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .collect(toList());
                return new ProviderResponse(provider, items);
            })
            .collect(toList());

        return new CartResponse(id, providers);
    }

    private String cartKey(UUID id) {
        return String.format("cart:%s", id);
    }

    private String providerKey(UUID id, String provider) {
        return String.format("cart:%s:provider:%s", id, provider);
    }

}
