package io.cartel.noord.brabant.domain.services;

import io.cartel.noord.brabant.api.dtos.DiffResponse;
import io.cartel.noord.brabant.api.dtos.Difference;
import io.cartel.noord.brabant.domain.entities.DiffSide;
import io.cartel.noord.brabant.domain.exceptions.DiffSideNotFoundException;
import io.cartel.noord.brabant.domain.exceptions.InvalidBase64Exception;
import io.cartel.noord.brabant.domain.exceptions.InvalidJsonException;
import io.cartel.noord.brabant.domain.repositories.DiffSideRepository;
import io.cartel.noord.brabant.api.enums.DiffResult;
import io.cartel.noord.brabant.domain.enums.Side;
import io.cartel.noord.brabant.shared.helpers.RandomHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static io.cartel.noord.brabant.shared.helpers.Base64Helper.encodeB64;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * {@link DiffServiceTest} Unit tests service logic and sequence of events. All dependencies are mocked away, they
 * have their own unit/integration tests.
 *
 * <p>All unit tests here are somewhat tested at API level as integration tests. Having these kind of redundant tests
 * is helpful for quick test cycles, or logic specific testing. It just need to be leveraged to not become a burden.
 */
@DisplayName("Diff Service")
class DiffServiceTest {

    @Mock
    private DiffSideRepository sideRepository;

    @Mock
    private DiffCheckService checkService;

    private DiffService service;

    @BeforeEach
    public void setup() {
        openMocks(this);

        service = new DiffService(sideRepository, checkService);
    }

    @Nested
    @DisplayName("when saving left")
    class SaveLeft {

        @Test
        @DisplayName("should persist received data")
        public void shouldPersist() {
            var testId = RandomHelper.uuid();
            var testData = RandomHelper.json();

            service.saveLeft(testId, encodeB64(testData));

            verify(sideRepository).save(eq(new DiffSide(Side.LEFT, testId, testData)));
        }

        @Test
        @DisplayName("should throw error if data is not Base64")
        public void shouldThrowB64Exception() {
            assertThrows(
                    InvalidBase64Exception.class,
                    () -> service.saveLeft(RandomHelper.uuid(), RandomHelper.json())
            );

            verifyNoInteractions(sideRepository);
        }

        @Test
        @DisplayName("should throw error if data is not JSON")
        public void shouldThrowJSONException() {
            assertThrows(
                    InvalidJsonException.class,
                    () -> service.saveLeft(RandomHelper.uuid(), encodeB64(RandomHelper.string()))
            );

            verifyNoInteractions(sideRepository);
        }
    }

    @Nested
    @DisplayName("when saving right")
    class SaveRight {

        @Test
        @DisplayName("should persist received data")
        public void shouldPersist() {
            var testId = RandomHelper.uuid();
            var testData = RandomHelper.json();

            service.saveRight(testId, encodeB64(testData));

            verify(sideRepository).save(eq(new DiffSide(Side.RIGHT, testId, testData)));
        }

        @Test
        @DisplayName("should thrown error if data is not Base64")
        public void shouldThrowB64Exception() {
            assertThrows(
                    InvalidBase64Exception.class,
                    () -> service.saveRight(RandomHelper.uuid(), RandomHelper.json())
            );

            verifyNoInteractions(sideRepository);
        }

        @Test
        @DisplayName("should thrown error if data is not JSON")
        public void shouldThrowJSONException() {
            assertThrows(
                    InvalidJsonException.class,
                    () -> service.saveRight(RandomHelper.uuid(), encodeB64(RandomHelper.string()))
            );

            verifyNoInteractions(sideRepository);
        }
    }

    @Nested
    @DisplayName("when getting diff by ID")
    class GetById {

        @Test
        @DisplayName("should throw an exception if left side is not found")
        public void shouldThrowNotFoundForLeftSides() {
            var testId = RandomHelper.uuid();

            withDiffSides(testId, null, RandomHelper.json());

            assertThrows(DiffSideNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        @DisplayName("should throw an exception if right side is not found")
        public void shouldThrowNotFoundForRightSides() {
            var testId = RandomHelper.uuid();

            withDiffSides(testId, RandomHelper.json(), null);

            assertThrows(DiffSideNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        @DisplayName("should return diff when both sides are present")
        public void shouldReturnDiff() {
            var testId = RandomHelper.uuid();
            var testData = RandomHelper.json();

            withDiffSides(testId, testData, testData);

            var expectedDiff = withCheckedDiffFor(testData);

            Assertions.assertEquals(expectedDiff, service.getById(testId));
        }

        private void withDiffSides(UUID id, String left, String right) {
            when(sideRepository.fetchDataBySideAndDiffId(eq(Side.LEFT), eq(id)))
                    .thenReturn(Optional.ofNullable(left));

            when(sideRepository.fetchDataBySideAndDiffId(eq(Side.RIGHT), eq(id)))
                    .thenReturn(Optional.ofNullable(right));
        }

        private DiffResponse withCheckedDiffFor(String testData) {
            var fakeDiff = new DiffResponse(DiffResult.EQUAL, singletonList(new Difference(30L, 6L)));

            when(checkService.getDiff(eq(testData), eq(testData)))
                    .thenReturn(fakeDiff);

            return fakeDiff;
        }
    }
}