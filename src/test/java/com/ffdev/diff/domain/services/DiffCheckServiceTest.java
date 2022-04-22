package com.ffdev.diff.domain.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ffdev.diff.api.enums.DiffResult.*;
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

        assertEquals(EQUAL, result.result());
        assertTrue(result.differences().isEmpty());
    }

    @Test
    @DisplayName("should return different sizes result")
    public void shouldReturnDifferentSizes() {
        var result = service.getDiff("some-data", "other-data");

        // some-data (9 chars) != other-data (10 chars)
        assertEquals(DIFFERENT_SIZES, result.result());

        // no differences are identified at the moment
        assertTrue(result.differences().isEmpty());
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

        assertEquals(DIFFERENT, result.result());
        assertEquals(1, result.differences().size());
        // som >> cen
        assertEquals(0L, result.differences().get(0).offset());
        assertEquals(3L, result.differences().get(0).length());
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

        assertEquals(DIFFERENT, result.result());
        assertEquals(1, result.differences().size());
        // data >> yolo
        assertEquals(5L, result.differences().get(0).offset());
        assertEquals(4L, result.differences().get(0).length());
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

        assertEquals(DIFFERENT, result.result());
        assertEquals(1, result.differences().size());
        // me-da >> lo:be
        assertEquals(2L, result.differences().get(0).offset());
        assertEquals(5L, result.differences().get(0).length());
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

        assertEquals(DIFFERENT, result.result());
        assertEquals(1, result.differences().size());
        assertEquals(0L, result.differences().get(0).offset());
        assertEquals(9L, result.differences().get(0).length());
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

        assertEquals(DIFFERENT, result.result());
        assertEquals(3, result.differences().size());
        // s >> i
        assertEquals(0L, result.differences().get(0).offset());
        assertEquals(1L, result.differences().get(0).length());
        // me >> ta
        assertEquals(2L, result.differences().get(1).offset());
        assertEquals(2L, result.differences().get(1).length());
        // dat >> bel
        assertEquals(5L, result.differences().get(2).offset());
        assertEquals(3L, result.differences().get(2).length());
    }
}