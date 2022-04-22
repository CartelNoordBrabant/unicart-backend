package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.api.dtos.AddItemPayload;
import io.cartel.noord.brabant.api.dtos.CartResponse;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    @PostMapping("{id}")
    public void saveItem(@PathVariable UUID id, @RequestBody AddItemPayload payload) {
    }

    @GetMapping("{id}")
    public CartResponse getCart(@PathVariable UUID id) {
        return null;
    }

    @PostMapping("{id}/checkout")
    public void checkout(@PathVariable UUID id) {
    }
}
