package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.shared.AbstractRedisIT;
import io.cartel.noord.brabant.api.enums.ErrorCode;
import io.cartel.noord.brabant.shared.helpers.RandomHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

/**
 * {@link DiffControllerIT} Integration tests the functionality as whole. So no mocks are used and the server is
 * started at a random port.
 *
 * <p>A real redis instance is used, and should be available to this integration test.
 *
 * <p>Using a solution like "testcontainers" would remove the need of a redis instance running locally.
 */
@DisplayName("Diff API")
class DiffControllerIT extends AbstractRedisIT {

//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Nested
//    @DisplayName("when posting should error")
//    class PostError {
//
//        @Test
//        @DisplayName("with 400 if left side is not Base64")
//        public void shouldReturn400IfLeftNotBase64() {
//            var response = postLeft(RandomHelper.uuid(), RandomHelper.json(), ErrorResponse.class);
//            assertEquals(BAD_REQUEST, response.getStatusCode());
//
//            var error = response.getBody();
//            assertNotNull(error);
//            Assertions.assertEquals(ErrorCode.BASE64_INVALID, error.code());
//            assertEquals("Invalid base 64 data", error.message());
//        }
//
//        @Test
//        @DisplayName("with 400 if right side is not Base64")
//        public void shouldReturn400IfRightNotBase64() {
//            var response = postRight(RandomHelper.uuid(), RandomHelper.json(), ErrorResponse.class);
//            assertEquals(BAD_REQUEST, response.getStatusCode());
//
//            var error = response.getBody();
//            assertNotNull(error);
//            Assertions.assertEquals(ErrorCode.BASE64_INVALID, error.code());
//            assertEquals("Invalid base 64 data", error.message());
//        }
//
//        @Test
//        @DisplayName("with 400 if left side is not JSON")
//        public void shouldReturn400IfLeftNotJSON() {
//            var response = postLeft(RandomHelper.uuid(), encodeB64(RandomHelper.string()), ErrorResponse.class);
//            assertEquals(BAD_REQUEST, response.getStatusCode());
//
//            var error = response.getBody();
//            assertNotNull(error);
//            assertEquals(ErrorCode.JSON_INVALID, error.code());
//            assertEquals("Invalid JSON data", error.message());
//        }
//
//        @Test
//        @DisplayName("with 400 if right side is not JSON")
//        public void shouldReturn400IfRightNotJSON() {
//            var response = postRight(RandomHelper.uuid(), encodeB64(RandomHelper.string()), ErrorResponse.class);
//            assertEquals(BAD_REQUEST, response.getStatusCode());
//
//            var error = response.getBody();
//            assertNotNull(error);
//            assertEquals(ErrorCode.JSON_INVALID, error.code());
//            assertEquals("Invalid JSON data", error.message());
//        }
//    }
//
//    @Nested
//    @DisplayName("when getting should error")
//    class GetError {
//
//        @Test
//        @DisplayName("with 404 if right side is missing")
//        public void shouldReturn404IfNoRightSide() {
//            var testId = RandomHelper.uuid();
//            var testData = encodeB64(RandomHelper.json());
//
//            assertEquals(ACCEPTED, postLeft(testId, testData));
//
//            var response = getDiff(testId, ErrorResponse.class);
//            assertEquals(NOT_FOUND, response.getStatusCode());
//
//            var error = response.getBody();
//            assertNotNull(error);
//            Assertions.assertEquals(ErrorCode.RIGHT_NOT_FOUND, error.code());
//            assertEquals("Diff right side was not found", error.message());
//        }
//
//        @Test
//        @DisplayName("with 404 if left side is missing")
//        public void shouldReturn404IfNoLeftSide() {
//            var testId = RandomHelper.uuid();
//            var testData = encodeB64(RandomHelper.json());
//
//            assertEquals(ACCEPTED, postRight(testId, testData));
//
//            var response = getDiff(testId, ErrorResponse.class);
//            assertEquals(NOT_FOUND, response.getStatusCode());
//
//            var error = response.getBody();
//            assertNotNull(error);
//            Assertions.assertEquals(ErrorCode.LEFT_NOT_FOUND, error.code());
//            assertEquals("Diff left side was not found", error.message());
//        }
//    }
//
//    @Nested
//    @DisplayName("should return diff with 200")
//    class CheckDiff {
//
//        @Test
//        @DisplayName("and result EQUAL when both sides are the same")
//        public void shouldReturn200WhenEqual() {
//            var testId = RandomHelper.uuid();
//            var testData = encodeB64(RandomHelper.json());
//
//            assertEquals(ACCEPTED, postLeft(testId, testData));
//            assertEquals(ACCEPTED, postRight(testId, testData));
//
//            var response = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, response.getStatusCode());
//
//            var diff = response.getBody();
//            assertNotNull(diff);
//            Assertions.assertEquals(DiffResult.EQUAL, diff.result());
//            assertTrue(diff.differences().isEmpty());
//        }
//
//        @Test
//        @DisplayName("and result DIFFERENT_SIZES when sides are not equivalent in length")
//        public void shouldReturn200WhenDifferentSizes() {
//            var testId = RandomHelper.uuid();
//            var leftData = "{\"id\":123,\"message\":\"some-data\"}";
//            var rightData = "{\"id\":132,\"message\":\"other-data\"}";
//
//            assertEquals(ACCEPTED, postLeft(testId, encodeB64(leftData)));
//            assertEquals(ACCEPTED, postRight(testId, encodeB64(rightData)));
//
//            var response = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, response.getStatusCode());
//
//            var diff = response.getBody();
//            assertNotNull(diff);
//            Assertions.assertEquals(DiffResult.DIFFERENT_SIZES, diff.result());
//            assertTrue(diff.differences().isEmpty());
//        }
//
//        @Test
//        @DisplayName("and result DIFFERENT when sides are the same size but not equal")
//        public void shouldReturn200WhenDifferent() {
//            var testId = RandomHelper.uuid();
//            var lData = "{\"id\":123,\"message\":\"some json\"}";
//            //                      ||     |               ||||
//            var rData = "{\"id\":213,\"massage\":\"some JSON\"}";
//
//            assertEquals(ACCEPTED, postLeft(testId, encodeB64(lData)));
//            assertEquals(ACCEPTED, postRight(testId, encodeB64(rData)));
//
//            var response = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, response.getStatusCode());
//
//            var diff = response.getBody();
//            assertNotNull(diff);
//            Assertions.assertEquals(DiffResult.DIFFERENT, diff.result());
//            assertEquals(3, diff.differences().size());
//            // 12 >> 21
//            assertEquals(6L, diff.differences().get(0).offset());
//            assertEquals(2L, diff.differences().get(0).length());
//            // e >> a
//            assertEquals(12L, diff.differences().get(1).offset());
//            assertEquals(1L, diff.differences().get(1).length());
//            // json >> JSON
//            assertEquals(26L, diff.differences().get(2).offset());
//            assertEquals(4L, diff.differences().get(2).length());
//        }
//    }
//
//    @Nested
//    @DisplayName("when caching")
//    class Caching {
//
//        @Test
//        @DisplayName("should not change diff in subsequent request")
//        public void shouldReturn200WithCache() {
//            var testId = RandomHelper.uuid();
//            var testData = encodeB64(RandomHelper.json());
//
//            assertEquals(ACCEPTED, postLeft(testId, testData));
//            assertEquals(ACCEPTED, postRight(testId, testData));
//
//            var response = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, response.getStatusCode());
//
//            var cacheResponse = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, cacheResponse.getStatusCode());
//
//            assertEquals(response.getBody(), cacheResponse.getBody());
//        }
//
//        @Test
//        @DisplayName("should evict and recalculate diff if one of its sides changed")
//        public void shouldRecalculateDiff() {
//            var testId = RandomHelper.uuid();
//            var testData = encodeB64(RandomHelper.json());
//
//            assertEquals(ACCEPTED, postLeft(testId, testData));
//            assertEquals(ACCEPTED, postRight(testId, testData));
//
//            var response = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, response.getStatusCode());
//
//            assertEquals(ACCEPTED, postLeft(testId, encodeB64(RandomHelper.json())));
//
//            var newResponse = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, newResponse.getStatusCode());
//
//            assertNotEquals(response.getBody(), newResponse.getBody());
//        }
//
//        @Test
//        @DisplayName("last diff should still be available if new diff side post errors out")
//        public void shouldNotRecalculateDiff() {
//            var testId = RandomHelper.uuid();
//            var testData = encodeB64(RandomHelper.json());
//
//            assertEquals(ACCEPTED, postLeft(testId, testData));
//            assertEquals(ACCEPTED, postRight(testId, testData));
//
//            var response = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, response.getStatusCode());
//
//            assertEquals(BAD_REQUEST, postLeft(testId, RandomHelper.string()));
//
//            var cacheResponse = getDiff(testId, DiffResponse.class);
//            assertEquals(OK, cacheResponse.getStatusCode());
//
//            assertEquals(response.getBody(), cacheResponse.getBody());
//        }
//    }
//
//    private <T> ResponseEntity<T> getDiff(UUID id, Class<T> clazz) {
//        var url = "http://localhost:" + port + "/v1/diff/{id}";
//        return restTemplate.getForEntity(url, clazz, id);
//    }
//
//    private HttpStatus postLeft(UUID id, String data) {
//        return postLeft(id, data, String.class).getStatusCode();
//    }
//
//    private <T> ResponseEntity<T> postLeft(UUID id, String data, Class<T> clazz) {
//        var body = new HttpEntity<>(data);
//        var url = "http://localhost:" + port + "/v1/diff/{id}/left";
//
//        return restTemplate.postForEntity(url, body, clazz, id);
//    }
//
//    private HttpStatus postRight(UUID id, String data) {
//        return postRight(id, data, String.class).getStatusCode();
//    }
//
//    private <T> ResponseEntity<T> postRight(UUID id, String data, Class<T> clazz) {
//        var body = new HttpEntity<>(data);
//        var url = "http://localhost:" + port + "/v1/diff/{id}/right";
//
//        return restTemplate.postForEntity(url, body, clazz, id);
//    }
}