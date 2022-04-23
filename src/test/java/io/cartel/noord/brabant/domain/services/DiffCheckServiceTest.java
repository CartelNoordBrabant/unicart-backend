package io.cartel.noord.brabant.domain.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link DiffServiceTest} Unit tests diff algorithm logic.
 *
 * <p>Not every tests case here is tested at API level as integration tests.
 * More granular tests are more useful in this case.
 *
 * <p>Random test cases where not used here as it would make the tests more difficult to visualize.
 */
@DisplayName("Diff Check Service")
class DiffCheckServiceTest {

    private final DiffCheckService service = new DiffCheckService();

    @Test
    @DisplayName("should return equal result")
    public void shouldReturnEqual() {
        var result = service.getDiff("some-data", "some-data");

        Assertions.assertEquals(DiffResult.EQUAL, result.result());
        Assertions.assertTrue(result.differences().isEmpty());
    }

    @Test
    @DisplayName("should return different sizes result")
    public void shouldReturnDifferentSizes() {
        var result = service.getDiff("some-data", "other-data");

        // some-data (9 chars) != other-data (10 chars)
        Assertions.assertEquals(DiffResult.DIFFERENT_SIZES, result.result());

        // no differences are identified at the moment
        Assertions.assertTrue(result.differences().isEmpty());
    }

    @Test
    @DisplayName("should return difference at the start")
    public void shouldReturnDifferenceAtStart() {
        var lData = "some-data";
        //           |||
        var rData = "cene-data";
        //           |||
        // offsets   012345678

        var result = service.getDiff(lData, rData);

        Assertions.assertEquals(DiffResult.DIFFERENT, result.result());
        Assertions.assertEquals(1, result.differences().size());
        // som >> cen
        Assertions.assertEquals(0L, result.differences().get(0).offset());
        Assertions.assertEquals(3L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return difference at the end")
    public void shouldReturnDifferenceAtEnd() {
        var lData = "some-data";
        //                ||||
        var rData = "some-yolo";
        //                ||||
        // offsets   012345678

        var result = service.getDiff(lData, rData);

        Assertions.assertEquals(DiffResult.DIFFERENT, result.result());
        Assertions.assertEquals(1, result.differences().size());
        // data >> yolo
        Assertions.assertEquals(5L, result.differences().get(0).offset());
        Assertions.assertEquals(4L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return difference in the middle")
    public void shouldReturnDifferenceInMiddle() {
        var lData = "some-data";
        //             |||||
        var rData = "solo:beta";
        //             |||||
        // offsets   012345678

        var result = service.getDiff(lData, rData);

        Assertions.assertEquals(DiffResult.DIFFERENT, result.result());
        Assertions.assertEquals(1, result.differences().size());
        // me-da >> lo:be
        Assertions.assertEquals(2L, result.differences().get(0).offset());
        Assertions.assertEquals(5L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return difference in the entire string")
    public void shouldReturnDifferenceInEntireString() {
        var lData = "some-data";
        //           |||||||||
        var rData = "atad_emos";
        //           |||||||||
        // offsets   012345678

        var result = service.getDiff(lData, rData);

        Assertions.assertEquals(DiffResult.DIFFERENT, result.result());
        Assertions.assertEquals(1, result.differences().size());
        Assertions.assertEquals(0L, result.differences().get(0).offset());
        Assertions.assertEquals(9L, result.differences().get(0).length());
    }

    @Test
    @DisplayName("should return multiple differences")
    public void shouldReturnMultipleChanges() {
        var lData = "some-data";
        //           | || |||
        var rData = "iota-bela";
        //           | || |||
        // offsets   012345678

        var result = service.getDiff(lData, rData);

        Assertions.assertEquals(DiffResult.DIFFERENT, result.result());
        Assertions.assertEquals(3, result.differences().size());
        // s >> i
        Assertions.assertEquals(0L, result.differences().get(0).offset());
        Assertions.assertEquals(1L, result.differences().get(0).length());
        // me >> ta
        Assertions.assertEquals(2L, result.differences().get(1).offset());
        Assertions.assertEquals(2L, result.differences().get(1).length());
        // dat >> bel
        Assertions.assertEquals(5L, result.differences().get(2).offset());
        Assertions.assertEquals(3L, result.differences().get(2).length());
    }
}