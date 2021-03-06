package io.cartel.noord.brabant.domain.services;

import io.cartel.noord.brabant.domain.entities.Cart;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.repositories.CartRepository;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository repository;

    public CartService(CartRepository repository) {
        this.repository = repository;
    }

    public void addItem(
        @NotNull UUID id,
        @NotNull String providerId,
        @NotNull UUID customerId,
        @NotNull Item item
    ) {
        repository.addItem(id, providerId, customerId, item);
    }

    public void removeItem(
        @NotNull UUID id,
        @NotNull String providerId,
        @NotNull String code
    ) {
        repository.removeItem(id, providerId, code);
    }

    public Cart getCart(@NotNull UUID id) {
        return repository.getCart(id);
    }

    public void doCheckout(UUID id) {
        repository.removeCart(id);
    }
}
