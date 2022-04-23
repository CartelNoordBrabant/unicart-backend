package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.api.dtos.ItemDTO;
import io.cartel.noord.brabant.api.dtos.PaymentDTO;
import io.cartel.noord.brabant.domain.entities.Item;
import io.cartel.noord.brabant.domain.services.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/checkout")
public class CheckoutController {

    private final PaymentService paymentService;

    public CheckoutController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/order/{id}")
    public void checkout(@PathVariable UUID orderId, @RequestBody PaymentDTO payload) {

    }

    private Item mapFromDTO(PaymentDTO payment) {
        return new Item(
                payment.name(),
                payment.code(),
                payment.imageURL(),
                payment.price(),
                payment.amount()
        );
    }
}
