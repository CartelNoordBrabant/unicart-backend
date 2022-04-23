package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.shared.AbstractRedisIT;
import io.cartel.noord.brabant.shared.helpers.RandomHelper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.*;

@DisplayName("UniCart API V1")
class CartControllerIT extends AbstractRedisIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Add Item")
    class AddItem {

        @Test
        @DisplayName("returns 200 when items is added")
        public void shouldReturn200WhenItemIsAdded() {
            var status = postAddItem(
                RandomHelper.uuid(),
                RandomHelper.string(),
                Map.of(
                    "customerId", RandomHelper.uuid(),
                    "item", Map.of(
                        "name", RandomHelper.string(),
                        "code", RandomHelper.string(),
                        "imageURL", RandomHelper.string(),
                        "price", RandomHelper.price(),
                        "amount", RandomHelper.quantity()
                    )
                )
            );
            assertEquals(OK, status);
        }

        @Test
        @DisplayName("returns 400 when customer is not UUID")
        public void shouldReturn400IfRightNotBase64() {
            var status = postAddItem(
                RandomHelper.uuid(),
                RandomHelper.string(),
                Map.of(
                    "customerId", RandomHelper.string(),
                    "item", Map.of(
                        "name", RandomHelper.string(),
                        "code", RandomHelper.string(),
                        "imageURL", RandomHelper.string(),
                        "price", RandomHelper.price(),
                        "amount", RandomHelper.quantity()
                    )
                )
            );
            assertEquals(BAD_REQUEST, status);
        }
    }

    @Nested
    @DisplayName("Remove Item")
    class RemoveItem {

        @Test
        @DisplayName("returns 200 when item is deleted")
        public void shouldReturn200WhenItemRemoved() {
            var status = deleteRemoveItem(
                RandomHelper.uuid(),
                RandomHelper.string(),
                RandomHelper.string()
            );
            assertEquals(OK, status);
        }
    }

    @Nested
    @DisplayName("Get Cart")
    class GetCart {

        @Test
        @DisplayName("returns 200 when cart exists")
        public void shouldReturn200WhenItemIsAdded() {
            var cart = RandomHelper.uuid();
            postAddItem(
                cart,
                RandomHelper.string(),
                Map.of(
                    "customerId", RandomHelper.uuid(),
                    "item", Map.of(
                        "name", RandomHelper.string(),
                        "code", RandomHelper.string(),
                        "imageURL", RandomHelper.string(),
                        "price", RandomHelper.price(),
                        "amount", RandomHelper.quantity()
                    )
                )
            );
            postAddItem(
                cart,
                RandomHelper.string(),
                Map.of(
                    "customerId", RandomHelper.uuid(),
                    "item", Map.of(
                        "name", RandomHelper.string(),
                        "code", RandomHelper.string(),
                        "imageURL", RandomHelper.string(),
                        "price", RandomHelper.price(),
                        "amount", RandomHelper.quantity()
                    )
                )
            );


            assertEquals(OK, getCart(cart));
        }

        @Test
        @DisplayName("returns 404 when cart does not exist")
        public void shouldReturn404WhenCartDoesNotExist() {
            assertEquals(NOT_FOUND, getCart(RandomHelper.uuid()));
        }
    }

    @Nested
    @DisplayName("Checkout")
    class Checkout {

        @Test
        @DisplayName("returns 200 when checkout is complete")
        public void shouldReturn200WhenItemIsAdded() {
            var cart = RandomHelper.uuid();
            postAddItem(
                cart,
                RandomHelper.string(),
                Map.of(
                    "customerId", RandomHelper.uuid(),
                    "item", Map.of(
                        "name", RandomHelper.string(),
                        "code", RandomHelper.string(),
                        "imageURL", RandomHelper.string(),
                        "price", RandomHelper.price(),
                        "amount", RandomHelper.quantity()
                    )
                )
            );

            assertEquals(OK, checkout(cart));
        }

        @Test
        @DisplayName("returns 404 after card is checked out")
        public void shouldReturn404WhenCartDoesNotExist() {
            var cart = RandomHelper.uuid();
            postAddItem(
                cart,
                RandomHelper.string(),
                Map.of(
                    "customerId", RandomHelper.uuid(),
                    "item", Map.of(
                        "name", RandomHelper.string(),
                        "code", RandomHelper.string(),
                        "imageURL", RandomHelper.string(),
                        "price", RandomHelper.price(),
                        "amount", RandomHelper.quantity()
                    )
                )
            );

            checkout(cart);

            assertEquals(NOT_FOUND, getCart(cart));
        }
    }

    private HttpStatus postAddItem(UUID id, String providerId, Map<String, Object> payload) {
        var body = new HttpEntity<>(payload);
        var url = "http://localhost:" + port + "/v1/cart/{id}/provider/{providerId}";

        var response = restTemplate.postForEntity(url, body, Void.class, id, providerId);
        return response.getStatusCode();
    }

    private HttpStatus deleteRemoveItem(UUID id, String providerId, String itemCode) {
        var url = "http://localhost:" + port + "/v1/cart/{id}/provider/{providerId}/item/{itemCode}";

        var response = restTemplate.exchange(url, DELETE, HttpEntity.EMPTY, Void.class, id, providerId, itemCode);
        return response.getStatusCode();
    }

    private HttpStatus getCart(UUID id) {
        var url = "http://localhost:" + port + "/v1/cart/{id}";

        var response = restTemplate.getForEntity(url, String.class, id);
        return response.getStatusCode();
    }

    private HttpStatus checkout(UUID id) {
        var url = "http://localhost:" + port + "/v1/cart/{id}/checkout";

        var response = restTemplate.postForEntity(url, HttpEntity.EMPTY, Void.class, id);
        return response.getStatusCode();
    }
}