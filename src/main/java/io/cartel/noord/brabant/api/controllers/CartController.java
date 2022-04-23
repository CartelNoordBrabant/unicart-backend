package io.cartel.noord.brabant.api.controllers;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import io.cartel.noord.brabant.api.dtos.AddItemDTO;
import io.cartel.noord.brabant.api.dtos.CartDTO;
import io.cartel.noord.brabant.api.dtos.ItemDTO;
import io.cartel.noord.brabant.api.dtos.ProviderDTO;
import io.cartel.noord.brabant.domain.entities.Cart;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.entities.Provider;
import io.cartel.noord.brabant.domain.services.CartService;
import java.util.UUID;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
    origins = "*",
    methods = { POST, GET, DELETE, HEAD, OPTIONS }
)
@RestController
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("{id}/provider/{providerId}")
    public void addItem(
        @PathVariable UUID id,
        @PathVariable String providerId,
        @RequestBody AddItemDTO payload
    ) {
        cartService.addItem(id, providerId, payload.customerId(), mapFromDTO(payload.item()));
    }

    @DeleteMapping("{id}/provider/{providerId}/item/{itemCode}")
    public void removeItem(
        @PathVariable UUID id,
        @PathVariable String providerId,
        @PathVariable String itemCode
    ) {
        cartService.removeItem(id, providerId, itemCode);
    }

    @GetMapping("{id}")
    public CartDTO getCart(@PathVariable UUID id) {
        return mapToDTO(cartService.getCart(id));
    }

    @PostMapping("{id}/checkout")
    public void checkout(@PathVariable UUID id) {
        cartService.doCheckout(id);
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
            provider.id(),
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
