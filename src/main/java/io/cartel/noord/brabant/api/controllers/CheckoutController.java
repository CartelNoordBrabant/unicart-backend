package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.domain.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/v1/checkout")
public class CheckoutController {

    private final CartService cartService;

    public CheckoutController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/{id}")
    public void checkout(@PathVariable UUID cartId) {
        this.cartService.doCheckout(cartId);
    }
}
