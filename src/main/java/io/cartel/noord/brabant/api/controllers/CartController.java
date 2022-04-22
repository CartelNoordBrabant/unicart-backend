package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.api.dtos.CartResponse;
import io.cartel.noord.brabant.api.dtos.ItemPayload;
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

    @PostMapping("{id}/provider/{provider}")
    public void addItem(@PathVariable UUID id, @RequestBody ItemPayload payload) {
    }

    @DeleteMapping("{id}/provider/{provider}/item/{code}")
    public void removeItem(
        @PathVariable UUID id,
        @PathVariable String provider,
        @PathVariable String code) {
    }

    @GetMapping("{id}")
    public CartResponse getCart(@PathVariable UUID id) {
        return null;
    }

    @PostMapping("{id}/checkout")
    public void checkout(@PathVariable UUID id) {
    }
}
