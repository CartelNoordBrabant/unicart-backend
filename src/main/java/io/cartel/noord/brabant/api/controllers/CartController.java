package io.cartel.noord.brabant.api.controllers;

import static java.util.stream.Collectors.toList;

import io.cartel.noord.brabant.api.dtos.CartDTO;
import io.cartel.noord.brabant.api.dtos.ItemDTO;
import io.cartel.noord.brabant.api.dtos.ProviderDTO;
import io.cartel.noord.brabant.domain.entities.Cart;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.entities.Provider;
import io.cartel.noord.brabant.domain.services.CartService;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("{id}/provider/{provider}")
    public void addItem(
        @PathVariable UUID id,
        @PathVariable String provider,
        @RequestBody ItemDTO payload
    ) {
        cartService.addItem(id, provider, mapFromDTO(payload));
    }

    @DeleteMapping("{id}/provider/{provider}/item/{code}")
    public void removeItem(
        @PathVariable UUID id,
        @PathVariable String provider,
        @PathVariable String code
    ) {
        cartService.removeItem(id, provider, code);
    }

    @GetMapping("{id}")
    public CartDTO getCart(@PathVariable UUID id) {
        return mapToDTO(cartService.getCart(id));
    }

    @PostMapping("{id}/checkout")
    public void checkout(@PathVariable UUID id) {
    }

    private CartDTO mapToDTO(Cart cart) {
        return new CartDTO(
            cart.id(),
            cart.providers()
                .stream()
                .map(this::mapToDTO)
                .collect(toList())
        );
    }

    private ProviderDTO mapToDTO(Provider provider) {
        return new ProviderDTO(
            provider.provider(),
            provider.items()
                .stream()
                .map(this::mapToDTO)
                .collect(toList())
        );
    }

    private ItemDTO mapToDTO(Item item) {
        return new ItemDTO(
            item.name(),
            item.code(),
            item.imageURL(),
            item.price(),
            item.amount()
        );
    }

    private Item mapFromDTO(ItemDTO item) {
        return new Item(
            item.name(),
            item.code(),
            item.imageURL(),
            item.price(),
            item.amount()
        );
    }
}
